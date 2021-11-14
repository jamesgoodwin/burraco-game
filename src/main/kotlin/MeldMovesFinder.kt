import kotlin.math.max
import kotlin.math.min

class MeldMovesFinder {

    // todo - split move finding into 4 categories - meld new sequence, meld existing sequence,
    //  meld new combination, meld existing combination

    fun getAllMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): List<MeldMove> {
        val handBySuit = hand.groupBy { it.suit }
        val handByValue = hand.groupBy { it.value }
        val wildcards = hand.filter { it.wildcard }

        val meldsBySuit = melds.groupBy { it.suit }
        val meldsByValue = melds.groupBy { it.value }

        val allMoves = mutableListOf<MeldMove>()
        allMoves.let {
            it.addAll(getNewSequenceMeldMoves(handBySuit, wildcards, state))
            it.addAll(getMeldToExistingSequenceMoves(handBySuit, wildcards, meldsBySuit, state))
            it.addAll(getMeldToExistingCombinationMoves(handByValue, wildcards, meldsByValue, state))
            it.addAll(getNewCombinationMeldMoves(handByValue, wildcards, state))
        }

        return allMoves
    }

    fun getNewCombinationMeldMoves(
        handByValue: Map<PlayingCard.Value, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        state: State
    ): List<MeldMove> {
        return handByValue.map { handCards ->
            var allCombinations: MutableList<Triple<Int, List<PlayingCard>, List<PlayingCard>>>? = mutableListOf()
            if (handCards.value.size >= 3) {
                allCombinations?.add(Triple(-1, handCards.value, emptyList()))
            }
            if (handCards.value.size == 2 && wildcards.isNotEmpty()) {
                wildcards.forEach { wildcard ->
                    val cardsUsed = handCards.value.toMutableList()
                    cardsUsed.add(wildcard)
                    allCombinations?.add(Triple(-1, cardsUsed, emptyList()))
                }
            }
            if (allCombinations?.isEmpty() == true) allCombinations = null
            allCombinations
        }.filterNotNull()
            .flatten()
            .map {
                MeldAttempt(-1, it.second)
            }
            .filter { Meld(it.handCardsUsed).valid }
            .map {
                NewMeldMove(Meld(it.handCardsUsed), state)
            }.toList()
            .distinctBy { }
    }

    fun getMeldToExistingCombinationMoves(
        handByValue: Map<PlayingCard.Value, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        meldsByValue: Map<PlayingCard.Value?, List<Meld>>,
        state: State
    ): List<MeldMove> {
        return handByValue.map { handCards ->
            var allCombinations: MutableList<Triple<Int, List<PlayingCard>, List<PlayingCard>>>? = mutableListOf()
            meldsByValue[handCards.key]?.forEach {
                allCombinations?.add(Triple(-1, handCards.value, it.cards))
                if (wildcards.isNotEmpty()) {
                    wildcards.forEach { wildcard ->
                        val cardsUsed = handCards.value.toMutableList()
                        cardsUsed.add(wildcard)
                        allCombinations?.add(Triple(-1, cardsUsed, it.cards))
                    }
                }
            }
            if (allCombinations?.isEmpty() == true) allCombinations = null
            allCombinations
        }.filterNotNull()
            .flatten()
            .map {
                MeldAttempt(-1, it.second, it.third)
            }
            .filter { Meld(it.meldCombo).valid }
            .map {
                ExistingMeldMove(it, state)
            }.toList()
            .distinctBy { }
    }

    fun getNewSequenceMeldMoves(
        handBySuit: Map<PlayingCard.Suit?, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        state: State
    ) = handBySuit.map { handCards ->
        val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()
        // check for new melds
        if (handCards.value.size in 3..14 || (handCards.value.size == 2 && wildcards.size in 1..2)) {
            allCombinations.add(Triple(-1, handCards.value, emptyList()))
//            if (wildcards.isNotEmpty()) {
//                wildcards.forEach { wildcard ->
//                    allCombinations.add(Triple(-1, handCards.value + wildcard, emptyList()))
//                }
//            }
        }
        allCombinations
    }.flatten()
        .map {
            createWindows(it, wildcards)
        }
        .flatten()
        .filter { Meld(it.handCardsUsed).valid }
        .map {
            NewMeldMove(Meld(it.handCardsUsed), state)
        }.toList()

    fun getMeldToExistingSequenceMoves(
        handBySuit: Map<PlayingCard.Suit?, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        meldsBySuit: Map<PlayingCard.Suit?, List<Meld>>,
        state: State
    ) = handBySuit.map { handCards ->
        val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()
        // check for adding cards to existing melds
        meldsBySuit[handCards.key]?.forEach {
            allCombinations.add(Triple(-1, handCards.value, it.cards))
            if (wildcards.isNotEmpty()) {
                wildcards.forEach { wildcard ->
                    allCombinations.add(Triple(-1, handCards.value + wildcard, it.cards))
                }
            }
        }
        allCombinations
    }.flatten()
        .map {
            createWindows(it, wildcards)
        }
        .flatten()
        .filter { Meld(it.meldCombo).valid }
        .map {
            ExistingMeldMove(it, state)
        }.toList()

    private fun createWindows(
        values: Triple<Int, List<PlayingCard>, List<PlayingCard>>,
        wildcards: List<PlayingCard>
    ): List<MeldAttempt> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))

        if (values.third.isNotEmpty()) {
            windows.addAll(getWindowsWithExistingMeld(values))
        } else if (values.second.isNotEmpty()) {
            windows.addAll(getWindowsWithNewMeld(values.second, wildcards))
        }
        return windows.flatten()
            .map { MeldAttempt(values.first, it - values.third, values.third, it) }
    }

    private fun getWindowsWithNewMeld(
        hand: List<PlayingCard>,
        wildcards: List<PlayingCard>
    ): Collection<List<List<PlayingCard>>> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))
        // loop from current size down to meld size + 1
        val minSize = if (wildcards.isEmpty()) 3 else 2

        for (i in hand.size downTo minSize) {
            var handWindows = hand.windowed(i)
            if (wildcards.isNotEmpty()) {
                handWindows = handWindows.map { list ->
                    val newList = list.toMutableList()
                    wildcards.forEach {
                        newList.add(it)
                    }
                    newList.toList()
                }
            }
            windows.add(handWindows)
        }
        return windows
    }

    private fun getWindowsWithExistingMeld(
        values: Triple<Int, List<PlayingCard>, List<PlayingCard>>
    ): Collection<List<List<PlayingCard>>> {
        val allCards = (values.second + values.third).sorted()
        val firstCardIndex = allCards.indexOf(values.third.first())
        val lastCardIndex = allCards.indexOf(values.third.last())

        // check if there's a 1 space gap
//        val range = allCards.last().value.ordinal - allCards.first().value.ordinal

//        if (allCards.size == range + 1 && wildcards.isNotEmpty()) {
//            //use a wildcard
//        }

        val windows = mutableListOf(listOf(listOf<PlayingCard>()))
        // loop from current size down to meld size + 1
        for (i in allCards.size downTo values.third.size + 1) {
            val maxSearchDistance = i - values.third.size
            val subList = allCards.subList(
                max(0, firstCardIndex - maxSearchDistance),
                min(allCards.size, lastCardIndex + maxSearchDistance + 1)
            )
            windows.add(subList.windowed(i))
        }
        return windows
    }
}


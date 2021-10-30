import kotlin.math.max
import kotlin.math.min

class MeldMovesFinder {

    fun findMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): List<MeldMove> {
        val handBySuit = hand.groupBy { it.suit }
        val handByValue = hand.groupBy { it.value }
        val wildcards = hand.filter { it.wildcard }

        val meldsBySuit = melds.groupBy { it.suit }
        val meldsByValue = melds.groupBy { it.value }

        return sequenceMoves(handBySuit, wildcards, meldsBySuit, state) + combinationMoves(
            handByValue,
            wildcards,
            meldsByValue,
            state
        )
    }

    private fun combinationMoves(
        handByValue: Map<PlayingCard.Value, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        meldsByValue: Map<PlayingCard.Value?, List<Meld>>,
        state: State
    ): List<MeldMove> {
        return handByValue.map { handCards ->
            val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()
            if (handCards.value.size >= 3) {
                allCombinations.add(Triple(-1, handCards.value, emptyList()))
                if (wildcards.isNotEmpty()) {
                    wildcards.forEach { wildcard ->
                        allCombinations.add(Triple(-1, handCards.value + wildcard, emptyList()))
                    }
                }
            }

            meldsByValue[handCards.key]?.forEach {
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
                createWindows(it)
            }
            .flatten()
            .filter { Meld(it.meldCombo).valid }
            .map {
                val meldMove: MeldMove = if (it.existingMeld.isNotEmpty()) {
                    ExistingMeldMove(it, state)
                } else {
                    NewMeldMove(Meld(it.handCardsUsed), state)
                }
                meldMove
            }.toList()
    }

    private fun sequenceMoves(
        handBySuit: Map<PlayingCard.Suit?, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        meldsBySuit: Map<PlayingCard.Suit?, List<Meld>>,
        state: State
    ) = handBySuit.map { handCards ->
        val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()
        // check for new melds
        if (handCards.value.size in 3..14 || (handCards.value.size == 2 && wildcards.size in 1..2)) {
            allCombinations.add(Triple(-1, handCards.value, emptyList()))
            if (wildcards.isNotEmpty()) {
                wildcards.forEach { wildcard ->
                    allCombinations.add(Triple(-1, handCards.value + wildcard, emptyList()))
                }
            }
        }
        // check for adding cards to existing melds
        meldsBySuit[handCards.key]?.forEach {
//            it.cards + handCards.value
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
            createWindows(it)
        }
        .flatten()
        .filter { Meld(it.meldCombo).valid }
        .map {
            val meldMove: MeldMove = if (it.existingMeld.isNotEmpty()) {
                ExistingMeldMove(it, state)
            } else {
                NewMeldMove(Meld(it.handCardsUsed), state)
            }
            meldMove
        }.toList()

    private fun createWindows(
        values: Triple<Int, List<PlayingCard>, List<PlayingCard>>
    ): List<MeldAttempt> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))

        if (values.third.isNotEmpty()) {
            windows.addAll(getWindowsWithExistingMeld(values))
        } else {
            windows.addAll(getWindowsWithNewMeld(values.second))
        }
        return windows.flatten()
            .map { MeldAttempt(values.first, it - values.third, values.third, it) }
    }

    private fun getWindowsWithNewMeld(allCards: List<PlayingCard>): Collection<List<List<PlayingCard>>> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))
        // loop from current size down to meld size + 1
        for (i in allCards.size downTo 3) {
            windows.add(allCards.windowed(i))
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


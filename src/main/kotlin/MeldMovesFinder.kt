import kotlin.math.max
import kotlin.math.min

class MeldMovesFinder(val meldValidator: MeldValidator) {

    fun findMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): List<Move> {
        val handBySuit = hand.groupBy { it.suit }
//        val wildcards = hand.filter { it.value == PlayingCard.Value.TWO || it.value == PlayingCard.Value.JOKER }

        val meldsBySuit = melds.groupBy { it.suite() }

        return handBySuit.map { handCards ->
            val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()
            if (handCards.value.size > 2) {
                // todo - need to do windows as with existing meld
                allCombinations.add(Triple(-1, handCards.value, emptyList()))
            }
            meldsBySuit[handCards.key]?.forEach {
                it.cards + handCards.value
                allCombinations.add(Triple(-1, handCards.value, it.cards))
            }
            allCombinations
        }.flatten()
            .map {
                createWindows(it)
            }
            .flatten()
            .filter { meldValidator.isValid(it.meldCombo) }
            .map { ExistingMeldMove(meldValidator, it, state) }.toList()
    }

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

data class MeldAttempt(
    val index: Int = -1,
    var handCardsUsed: List<PlayingCard> = emptyList(),
    val existingMeld: List<PlayingCard> = emptyList(),
    val meldCombo: List<PlayingCard> = emptyList()
)
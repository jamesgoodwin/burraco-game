import kotlin.math.max
import kotlin.math.min

class MeldMovesFinder(val meldValidator: MeldValidator) {

    fun findMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): List<Move>? {
        val handBySuit = hand.groupBy { it.suit }
        val wildcards = hand.filter { it.value == PlayingCard.Value.TWO || it.value == PlayingCard.Value.JOKER }

        return melds.asSequence().mapIndexed { index, meld ->
            handBySuit[meld.suite()]?.let { handCards ->
                Triple(index, handCards, meld.cards)
            }
        }.mapNotNull {
            it?.let {
                createWindows(it, wildcards)
            }
        }.flatten()
            .filter { meldValidator.isValid(it.meldCombo) }
            .map { ExistingMeldMove(meldValidator, it, state) }.toList()
    }

    private fun createWindows(values: Triple<Int, List<PlayingCard>, List<PlayingCard>>, wildcards: List<PlayingCard>): List<MeldToExistingSummary> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))

        // loop from current size down to meld size + 1
        val allCards = (values.second + values.third).sorted()
        val firstCardIndex = allCards.indexOf(values.third.first())
        val lastCardIndex = allCards.indexOf(values.third.last())

        // check if there's a 1 space gap
        val range = allCards.last().value.ordinal - allCards.first().value.ordinal

        if (allCards.size == range + 1 && wildcards.isNotEmpty()) {
            //use a wildcard
        }

        for (i in allCards.size downTo values.third.size + 1) {
            val maxSearchDistance = i - values.third.size
            val subList = allCards.subList(
                max(0, firstCardIndex - maxSearchDistance),
                min(allCards.size, lastCardIndex + maxSearchDistance + 1)
            )
            windows.add(subList.windowed(i))
        }
        return windows.flatten()
            .map { MeldToExistingSummary(values.first, it - values.third, values.third, it) }
    }
}

data class MeldToExistingSummary(
    val index: Int,
    val handCardsUsed: List<PlayingCard>,
    val existingMeld: List<PlayingCard>,
    val meldCombo: List<PlayingCard>
)
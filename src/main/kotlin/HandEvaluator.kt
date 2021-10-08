import PlayingCard.Value.JOKER
import PlayingCard.Value.TWO

class HandEvaluator(private val meldValidator: MeldValidator) : MeldEvaluator {

    override fun getMelds(cards: List<PlayingCard>): List<List<PlayingCard>> {
        val wildcards = cards.filter { it.value == TWO || it.value == JOKER }.toMutableList()
        val minSize = when (wildcards.isNotEmpty()) {
            true -> 2; else -> 3
        }

        val cardsBySuit =
            cards.groupBy { it.suit }.mapValues { it.value.toHashSet().filterNot { c -> c.value == TWO }.sorted() }
                .filter { it.value.size >= minSize }

        val cardsByValue = cards.sorted().groupBy { it.value }.filter { it.value.size >= minSize }

        val melds = mutableListOf<List<PlayingCard>>()

        cardsBySuit.forEach { map ->
            for (i in map.value.size downTo minSize) { //consider no melds first
                map.value.windowed(i).forEach { cardWindow ->
                    val max = cardWindow.maxOf { it.value.ordinal }
                    val min = cardWindow.minOf { it.value.ordinal }
                    if ((max - min) == (cardWindow.size - 1)) {
                        melds.add(cardWindow)
                    } else if ((max - min) == (cardWindow.size) && wildcards.isNotEmpty()) {
                        melds.add(cardWindow + wildcards.first())
                    }
                }
            }
        }

        cardsByValue.forEach {
            melds.addAll(listOf(it.value))
        }

        return melds
    }

}
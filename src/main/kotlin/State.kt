class State(val players: List<Player>) {

    fun hand(player: Player): List<PlayingCard>? {
        return hands[player]
    }

    fun melds(player: Player): MutableList<MutableList<PlayingCard>>? {
        return melds[player]
    }

    fun printTotalCardCount() {
        val cardsInHands = players.mapNotNull { hand(it) }.map { it.size }.sumOf { it }

        val cardsInMelds = players.mapNotNull { melds(it) }.flatten().map { it.size }.sumOf { it }

        val cardsInPot = players.mapNotNull { pots[it] }.map { it.size }.sumOf { it }

        val totalCards = cardsInHands + cardsInMelds + cardsInPot + stock.size + discard.size
        println("T: $totalCards, H: $cardsInHands, M: $cardsInMelds, P:$cardsInPot, S:${stock.size}, D:${discard.size}")
    }

    private fun printPile() {
        if (discard.isNotEmpty()) {
            val cards = discard.joinToString(", ")
            println("Discard pile: $cards")
        }
    }

    fun printMelds(player: Player) {
        val melds = melds(player)
        if (melds?.isNotEmpty() == true) {
            val cards = melds.joinToString(" - ") { it.sorted().joinToString(", ") }
            println("Melds: $cards")
        }
    }

    private fun printHand(player: Player) {
        val cards = hand(player)?.sorted()
        val hand = cards?.joinToString(", ")
        println("Hand: $hand")
    }

    fun printGameState(player: Player) {
        printPile()
        printHand(player)
        printMelds(player)
    }

    fun hasBurraco(player: Player): Boolean {
        return melds[player]?.any { it.size >= 7 } == true
    }

    fun playerClosed(player: Player): Boolean {
        return pots[player]?.isEmpty() == true && hands[player]?.isEmpty() == true && hasBurraco(player)
    }

    fun burracos(player: Player): List<Burraco> {
        val melds = melds[player]?.filter { it.size >= 7 }

        melds?.forEach {
//            val meld = Meld(it)
//            if(meld.wildcards.isEmpty()) {
//                // pulito
//            } else
        }

        return emptyList()
    }

    var finished: Boolean = false
    var playersTurn: Player = players[0]

    val hands: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }
    val melds: Map<Player, MutableList<MutableList<PlayingCard>>> = players.associateWith { mutableListOf() }

    var stock = ArrayDeque<PlayingCard>()
    var discard = mutableListOf<PlayingCard>()
    val pots: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }

}

enum class Burraco(points: Int) {
    PULITO(200), SPORCO(100), SEMI_PULITO(150)
}

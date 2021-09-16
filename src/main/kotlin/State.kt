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

        println("Total cards: $totalCards")
    }

    var finished: Boolean = false
    var playersTurn: Player = players[0]

    val hands: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }
    val melds: Map<Player, MutableList<MutableList<PlayingCard>>> = players.associateWith { mutableListOf() }

    var stock = ArrayDeque<PlayingCard>()
    var discard = mutableListOf<PlayingCard>()
    val pots : Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }

}
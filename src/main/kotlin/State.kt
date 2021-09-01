class State {

    var playerTakenCards = false
    var playersTurn: Player

    var stock = ArrayDeque<PlayingCard>()
    var discard = mutableListOf<PlayingCard>()
    val pots = ArrayList<MutableList<PlayingCard>>()
    val players = listOf(Player(), Player())

    init {
        playersTurn = players[0]
    }

}
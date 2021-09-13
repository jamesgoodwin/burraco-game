class State {

    var playerTakenCards = false
    var playersTurn: HumanPlayer

    var stock = ArrayDeque<PlayingCard>()
    var discard = mutableListOf<PlayingCard>()
    val pots = ArrayList<MutableList<PlayingCard>>()
    val players = listOf(HumanPlayer(), HumanPlayer())

    init {
        playersTurn = players[0]
    }

}
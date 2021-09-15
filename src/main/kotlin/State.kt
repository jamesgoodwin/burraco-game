class State(val players: List<Player>) {

    fun hand(player: Player): List<PlayingCard>? {
        return hands[player]
    }

    fun melds(player: Player): MutableList<MutableList<PlayingCard>>? {
        return melds[player]
    }

    var finished: Boolean = false
    var playerTakenCards = false
    var playersTurn: Player = players[0]

    val hands: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }
    val melds: Map<Player, MutableList<MutableList<PlayingCard>>> = players.associateWith { mutableListOf() }

    var stock = ArrayDeque<PlayingCard>()
    var discard = mutableListOf<PlayingCard>()
    val pots : Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }

}
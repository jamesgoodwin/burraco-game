class DiscardMove(private val card: PlayingCard) : Move {

    override fun performMove(state: State): Boolean {
        state.discardCard(state.playersTurn, card)
        state.printTotalCardCount()

        return true
    }

}
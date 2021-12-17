class DiscardMove(private val card: PlayingCard) : Move() {

    override fun performMove(state: State): Boolean {
        state.discardCard(state.playersTurn, card)
//        state.printTotalCardCount()

        return true
    }

    override fun equals(other: Any?): Boolean {
        if(other is DiscardMove) {
            return other.card == this.card
        }
        return super.equals(other)
    }
}
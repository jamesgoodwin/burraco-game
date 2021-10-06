class TakePileMove(val state: State) : Move {

    override fun performMove(): Boolean {
        state.hands[state.playersTurn]?.addAll(state.discard)
        state.discard.clear()

        state.printTotalCardCount()
        return true
    }

}
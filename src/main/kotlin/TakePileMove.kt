class TakePileMove : Move {

    override fun performMove(state: State): Boolean {
        state.hands[state.playersTurn]?.addAll(state.discard)
        state.discard.clear()

        state.printTotalCardCount()
        return true
    }

}
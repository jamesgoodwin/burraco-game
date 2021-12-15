class TakePileMove : Move {

    override fun performMove(state: State): Boolean {
        state.takePile(state.playersTurn)
        state.printTotalCardCount()
        return true
    }

}
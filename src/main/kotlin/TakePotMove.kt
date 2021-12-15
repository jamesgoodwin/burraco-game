class TakePotMove : Move {

    override fun performMove(state: State): Boolean {
        state.takePot(state.playersTurn)
        return true
    }

}

class TakePotMove : Move() {

    override fun performMove(state: State): Boolean {
        state.takePot(state.playersTurn)
        return true
    }

    override fun equals(other: Any?): Boolean {
        if(other is TakePotMove) {
            return true
        }
        return super.equals(other)
    }

}

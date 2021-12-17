class TakePileMove : Move() {

    override fun performMove(state: State): Boolean {
        state.takePile(state.playersTurn)
//        state.printTotalCardCount()
        return true
    }

    override fun equals(other: Any?): Boolean {
        if(other is TakePileMove) {
            return true
        }
        return super.equals(other)
    }

}
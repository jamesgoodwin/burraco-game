class TakePileMove : Move() {

    override fun performMove(state: State): Boolean {
        state.takePile(state.playersTurn)
        return true
    }

    override fun toString(): String {
        return "Take pile"
    }

    override fun equals(other: Any?): Boolean {
        if(other is TakePileMove) {
            return true
        }
        return super.equals(other)
    }

}
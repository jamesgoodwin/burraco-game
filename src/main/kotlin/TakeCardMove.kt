class TakeCardMove : Move() {

    override fun performMove(state: State): Boolean {
        state.takeCard(state.playersTurn)
        return true
    }

    override fun toString(): String {
        return "Take card from deck"
    }

    override fun equals(other: Any?): Boolean {
        if(other is TakeCardMove) {
            return true
        }
        return super.equals(other)
    }

}
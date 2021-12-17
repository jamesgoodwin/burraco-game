class TakeCardMove : Move() {

    override fun performMove(state: State): Boolean {
        state.takeCard(state.playersTurn)
//        state.printTotalCardCount()
        return true
    }



    override fun equals(other: Any?): Boolean {
        if(other is TakeCardMove) {
            return true
        }
        return super.equals(other)
    }

}
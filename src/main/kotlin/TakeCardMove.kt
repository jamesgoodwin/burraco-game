class TakeCardMove : Move {

    override fun performMove(state: State): Boolean {
        state.takeCard(state.playersTurn)
        state.printTotalCardCount()
        return true
    }

}
class TakeCardMove : Move {

    override fun performMove(state: State): Boolean {
        state.hands[state.playersTurn]?.add(state.stock.removeLast())
        state.printTotalCardCount()
        return true
    }

}
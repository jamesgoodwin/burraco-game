class TakeCardMove(val state: State) : Move {

    override fun performMove(): Boolean {
        state.hands[state.playersTurn]?.add(state.stock.removeLast())
        state.printTotalCardCount()
        return true
    }

}
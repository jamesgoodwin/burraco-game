class TakePotMove(val state: State) : Move {

    override fun performMove(): Boolean {
        state.pots[state.playersTurn]?.toMutableList()?.let { state.hands[state.playersTurn]?.addAll(it) }
        state.pots[state.playersTurn]?.clear()
        return true
    }

}

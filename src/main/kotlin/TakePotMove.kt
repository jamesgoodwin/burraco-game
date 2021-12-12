class TakePotMove : Move {

    override fun performMove(state: State): Boolean {
        state.pots[state.playersTurn]?.toMutableList()?.let { state.hands[state.playersTurn]?.addAll(it) }
        state.pots[state.playersTurn]?.clear()
        return true
    }

}

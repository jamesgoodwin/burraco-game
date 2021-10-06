class MeldMove(val meldValidator: MeldValidator, val cards: List<PlayingCard>, val state: State) : Move {

    override fun performMove(): Boolean {
        if (state.hand(state.playersTurn)?.containsAll(cards) != true) return false

        if (meldValidator.isValid(cards)) {
            state.melds[state.playersTurn]?.add(cards.toMutableList())

            cards.forEach { card ->
                state.hands[state.playersTurn]?.remove(card)
            }

            state.printTotalCardCount()

            return true
        }
        return false
    }

}
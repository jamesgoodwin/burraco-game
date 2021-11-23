interface MeldMove : Move

class NewMeldMove(val meld: Meld, val state: State) : MeldMove {

    override fun performMove(): Boolean {
        if (state.hand(state.playersTurn)?.containsAll(meld.cards) != true) return false

        if (meld.valid) {
            state.melds[state.playersTurn]?.add(meld.cards.toMutableList())
            meld.cards.forEach { card ->
                state.hands[state.playersTurn]?.remove(card)
            }
            state.printTotalCardCount()
            return true
        }
        return false
    }

    override fun toString(): String {
        return "Meld $meld"
    }

}
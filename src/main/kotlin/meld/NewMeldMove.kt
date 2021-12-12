import meld.Meld

interface MeldMove : Move

class NewMeldMove(val meld: Meld) : MeldMove {

    override fun performMove(state: State): Boolean {
        if (!state.hand(state.playersTurn).containsAll(meld.cards)) return false

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
        return "meld.Meld $meld"
    }

}
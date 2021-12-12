package meld

import MeldMove
import State

class ExistingMeldMove(private val meldAttempt: MeldAttempt) : MeldMove {

    override fun performMove(state: State): Boolean {
        if (!state.hand(state.playersTurn).containsAll(meldAttempt.handCardsUsed)) return false

        val existingMeld = state.melds(state.playersTurn)[meldAttempt.index]
        existingMeld.addAll(meldAttempt.handCardsUsed)
        meldAttempt.handCardsUsed.forEach { card ->
            state.hands[state.playersTurn]?.remove(card)
        }
        return true
    }

    override fun toString(): String {
        val handCardsUsed = meldAttempt.handCardsUsed.joinToString(",")
        val existingMeld = meldAttempt.existingMeld.joinToString(",")
        return "Add $handCardsUsed to existing meld: $existingMeld"
    }

}

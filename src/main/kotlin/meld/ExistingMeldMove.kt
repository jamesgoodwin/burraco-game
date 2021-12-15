package meld

import State

class ExistingMeldMove(private val meldAttempt: MeldAttempt) : MeldMove {

    override fun performMove(state: State): Boolean {
        return state.meld(meldAttempt.handCardsUsed, meldAttempt.index)
    }


    override fun toString(): String {
        val handCardsUsed = meldAttempt.handCardsUsed.joinToString(",")
        val existingMeld = meldAttempt.existingMeld.joinToString(",")
        return "Add $handCardsUsed to existing meld: $existingMeld"
    }

}

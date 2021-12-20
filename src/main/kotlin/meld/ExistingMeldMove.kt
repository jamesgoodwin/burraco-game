package meld

import State

class ExistingMeldMove(private val meldAttempt: MeldAttempt) : MeldMove() {

    override fun performMove(state: State): Boolean {
        return state.meld(meldAttempt.handCardsUsed, meldAttempt.index)
    }

    override fun equals(other: Any?): Boolean {
        return this.toString() == other.toString()
    }

    override fun toString(): String {
        val handCardsUsed = meldAttempt.handCardsUsed.joinToString(",")
        val existingMeld = meldAttempt.existingMeld.joinToString(",")
        return "Add $handCardsUsed to existing meld: $existingMeld"
    }

    override fun hashCode(): Int {
        return meldAttempt.hashCode()
    }

}

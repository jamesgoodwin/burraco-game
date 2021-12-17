package meld

import Move
import State

abstract class MeldMove : Move()

class NewMeldMove(val meld: Meld) : MeldMove() {

    override fun performMove(state: State): Boolean {
        val melded = state.meld(state.playersTurn, meld.cards, meld.valid)

//        if(melded) state.printTotalCardCount()

        return melded
    }

    override fun equals(other: Any?): Boolean {
        if(other is NewMeldMove) {
            return this.meld == other.meld
        }
        return super.equals(other)
    }


    override fun toString(): String {
        return "Meld $meld"
    }

}
package meld

import Move
import State

interface MeldMove : Move

class NewMeldMove(val meld: Meld) : MeldMove {

    override fun performMove(state: State): Boolean {
        val melded = state.meld(state.playersTurn, meld.cards, meld.valid)

        if(melded) state.printTotalCardCount()

        return melded
    }

    override fun toString(): String {
        return "Meld $meld"
    }

}
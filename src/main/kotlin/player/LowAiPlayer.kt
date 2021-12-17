package player

import meld.MeldEvaluator
import State

class LowAiPlayer(private val meldEvaluator: MeldEvaluator) : Player {

    override fun takeTurn(state: State, turn: PlayerTurn) {
        if(state.discard.size > 3) {
            turn.takePile()
        } else {
            turn.takeCard()
        }

        state.hand(this).let { cards ->
            val melds = meldEvaluator.getMelds(cards)
            if (melds.isNotEmpty()) {
                turn.meld(melds.first())
            }
            println(state.meldsToString(this))
        }

        state.hand(this).first().let { card ->
            turn.discard(card)
        }

        state.advancePlayer()
        // given a list of cards
        // return a collection with the highest scoring hand
        // if other hands available and not intersecting, return them too

        // compare with just the cards in your hand to see if the
//        }
    }

    override fun name() = "LowPlayAi"

}
package player

import meld.MeldMove
import Move
import PlayingCard

interface PlayerTurn {

    fun performMove(move: Move)

    fun takeCard(): Boolean
    fun takePile(): Boolean
    fun discard(card: PlayingCard): Boolean
    fun meld(cards: List<PlayingCard>, i: Int): Boolean
    fun meld(cards: List<PlayingCard>): Boolean
    fun meld(move: MeldMove): Boolean
    fun getTurnState(): PlayerTurnState
    fun roundOver(): Boolean
}


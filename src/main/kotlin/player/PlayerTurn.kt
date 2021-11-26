package player

import MeldMove
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
}


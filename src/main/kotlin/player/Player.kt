package player

import State

interface Player {
    fun takeTurn(state: State, turn: PlayerTurn)
    fun name(): String
}
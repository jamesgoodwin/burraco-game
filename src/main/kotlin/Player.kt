interface Player {
    fun takeTurn(state: State, turn: PlayerTurn)
    fun name(): String
}
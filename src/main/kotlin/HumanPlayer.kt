data class HumanPlayer(val hand: MutableList<PlayingCard> = mutableListOf(),
                       val melds: MutableList<MutableList<PlayingCard>> = mutableListOf()) : Player {

    override fun takeTurn(turn: PlayerTurn) {
        turn.takeCard()
        turn.meld(listOf())
        turn.discard(hand[0])
    }

    fun takeCard(game: BurracoGame) {
        game.takeCard(this)
    }

    fun discard(game: BurracoGame) {
        game.discard(this, hand[0])
    }

    fun meld(game: BurracoGame, meld: List<PlayingCard>, index: Int? = null) {
        game.meld(this, meld, index)
    }

}

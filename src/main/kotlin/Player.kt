data class Player(val hand: MutableList<PlayingCard> = mutableListOf(),
                  val melds: MutableList<MutableList<PlayingCard>> = mutableListOf()) {

    fun takeCard(game: BurracoGame) {
        game.takeCard(this)
    }

    fun discard(game: BurracoGame) {
        game.discard(this, hand[0])
    }

    fun meld(game: BurracoGame, meld: List<PlayingCard>) {
        game.meld(this, meld)
    }

}

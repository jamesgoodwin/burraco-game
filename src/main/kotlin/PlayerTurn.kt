interface PlayerTurn {
    fun takeCard(): Boolean
    fun takePile(): Boolean
    fun discard(playingCard: PlayingCard): Boolean
    fun meld(playingCard: List<PlayingCard>, i: Int): Boolean
}


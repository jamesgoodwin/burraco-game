interface PlayerTurn {
    fun takeCard()
    fun discard(playingCard: PlayingCard)
    fun meld(playingCard: List<PlayingCard>)
}

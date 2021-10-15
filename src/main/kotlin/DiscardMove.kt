class DiscardMove(private val card: PlayingCard, private val state: State) : Move {

    override fun performMove(): Boolean {
        state.discard.add(card)
        state.hands[state.playersTurn]?.remove(card)

        state.printTotalCardCount()
        return true
    }

}
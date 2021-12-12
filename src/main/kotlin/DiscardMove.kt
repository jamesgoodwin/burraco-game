class DiscardMove(private val card: PlayingCard) : Move {

    override fun performMove(state: State): Boolean {
        state.discard.add(card)
        state.hands[state.playersTurn]?.remove(card)

        state.printTotalCardCount()
        return true
    }

}
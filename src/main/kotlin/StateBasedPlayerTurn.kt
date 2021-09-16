class StateBasedPlayerTurn(private val state: State, private val meldValidator: MeldValidator) : PlayerTurn {

    var takenCard: Boolean = false
    var discardedCard: Boolean = false

    override fun takeCard(): Boolean {
        if (!takenCard) {
            state.hands[state.playersTurn]?.add(state.stock.removeLast())

            state.printTotalCardCount()

            takenCard = true
            return true
        }
        return false
    }

    override fun takePile(): Boolean {
        if (!takenCard) {
            state.hands[state.playersTurn]?.addAll(state.discard)
            state.discard.clear()
            takenCard = true

            state.printTotalCardCount()

            return true
        }
        return false
    }

    override fun discard(playingCard: PlayingCard): Boolean {
        if (takenCard && !discardedCard) {
            // if pot taken check if player has burraco, if not then cant discard final card
            state.discard.add(playingCard)
            state.hands[state.playersTurn]?.remove(playingCard)
            discardedCard = true

            state.printTotalCardCount()
            //check if hand empty and award pot if not already taken
            if(playerCanTakePot()) {
                state.pots[state.playersTurn]?.toMutableList()?.let { state.hands[state.playersTurn]?.addAll(it) }
                state.pots[state.playersTurn]?.clear()
            }

            return true
        }
        return false
    }

    private fun playerCanTakePot() = (state.hand(state.playersTurn)?.isEmpty() == true && !playerHasTakenPot())

    private fun playerHasTakenPot() = state.pots[state.playersTurn]?.isEmpty() == true

    override fun meld(playingCard: List<PlayingCard>, index: Int): Boolean {
        if (takenCard) {
            if (meldValidator.isValid(playingCard)) {
                state.melds[state.playersTurn]?.add(playingCard.toMutableList())
                state.hands[state.playersTurn]?.removeAll(playingCard)

                state.printTotalCardCount()

                return true
                //check if taken pot already
                // if not and hand is empty reset takenCard to allow more cards to be played
            }
        }
        return false
    }
}
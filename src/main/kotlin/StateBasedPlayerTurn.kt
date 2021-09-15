class StateBasedPlayerTurn(private val state: State, private val meldValidator: MeldValidator) : PlayerTurn {

    var takenCard: Boolean = false
    var discardedCard: Boolean = false

    override fun takeCard(): Boolean {
        if (!takenCard) {
            state.hands[state.playersTurn]?.add(state.stock.last())
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
            return true
        }
        return false
    }

    override fun discard(playingCard: PlayingCard): Boolean {
        if (takenCard && !discardedCard) {
            //if pot taken check if player has burraco, if not then cant discard final card
            state.discard.add(playingCard)
            state.hands[state.playersTurn]?.remove(playingCard)
            discardedCard = true
            return true
            //check if hand empty and award pot if not already taken
        }
        return false
    }

    override fun meld(playingCard: List<PlayingCard>, index: Int): Boolean {
        if (takenCard) {
            if (meldValidator.isValid(playingCard)) {
                state.melds[state.playersTurn]?.add(playingCard.toMutableList())
                state.hands[state.playersTurn]?.removeAll(playingCard)
                return true
                //check if taken pot already
                // if not and hand is empty reset takenCard to allow more cards to be played
            }
        }
        return false
    }
}
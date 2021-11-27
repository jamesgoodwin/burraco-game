import meld.Meld
import player.PlayerTurn

class StateBasedPlayerTurn(private val state: State) : PlayerTurn {

    var takenCard: Boolean = false
    var discardedCard: Boolean = false

    override fun performMove(move: Move) {
        move.performMove()
    }

    override fun takeCard(): Boolean {
        if (!takenCard) {
            performMove(TakeCardMove(state))
            takenCard = true
            return true
        }
        return false
    }

    override fun takePile(): Boolean {
        if (!takenCard) {
            performMove(TakePileMove(state))
            takenCard = true
            return true
        }
        return false
    }

    override fun discard(card: PlayingCard): Boolean {
        if (takenCard && !discardedCard) {
            performMove(DiscardMove(card, state))
            discardedCard = true

            if (playerCanTakePot()) {
                performMove(TakePotMove(state))
            }
            return true
        }
        return false
    }
    
    override fun meld(cards: List<PlayingCard>, i: Int): Boolean {
        if (takenCard) {
            performMove(NewMeldMove(Meld(cards), state))

            if (playerCanTakePot()) {
                performMove(TakePotMove(state))
            }
            return true
        }
        return false
    }

    override fun meld(cards: List<PlayingCard>): Boolean {
        if (takenCard) {
            performMove(NewMeldMove(Meld(cards), state))

            if (playerCanTakePot()) {
                performMove(TakePotMove(state))
            }
            return true
        }
        return false
    }

    override fun meld(move: MeldMove): Boolean {
        if (takenCard) {
            performMove(move)
            if (playerCanTakePot()) {
                performMove(TakePotMove(state))
            }
            return true
        }
        return false
    }

    private fun playerCanTakePot() = (state.hand(state.playersTurn).isEmpty() && !playerHasTakenPot())

    private fun playerHasTakenPot() = state.pots[state.playersTurn]?.isEmpty() == true
    
}
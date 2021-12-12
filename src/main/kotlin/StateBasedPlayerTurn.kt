import meld.Meld
import player.PlayerTurn
import player.PlayerTurnState

class StateBasedPlayerTurn(private val state: State) : PlayerTurn {

    var takenCard: Boolean = false
    var discardedCard: Boolean = false

    override fun performMove(move: Move) {
        move.performMove(state)
    }

    override fun takeCard(): Boolean {
        if (!takenCard) {
            performMove(TakeCardMove())
            takenCard = true
            return true
        }
        return false
    }

    override fun takePile(): Boolean {
        if (!takenCard) {
            performMove(TakePileMove())
            takenCard = true
            return true
        }
        return false
    }

    override fun discard(card: PlayingCard): Boolean {
        if (takenCard && !discardedCard) {
            performMove(DiscardMove(card))
            discardedCard = true

            if (playerCanTakePot()) {
                performMove(TakePotMove())
            }
            return true
        }
        return false
    }
    
    override fun meld(cards: List<PlayingCard>, i: Int): Boolean {
        if (takenCard) {
            performMove(NewMeldMove(Meld(cards)))

            if (playerCanTakePot()) {
                performMove(TakePotMove())
            }
            return true
        }
        return false
    }

    override fun meld(cards: List<PlayingCard>): Boolean {
        if (takenCard) {
            performMove(NewMeldMove(Meld(cards)))

            if (playerCanTakePot()) {
                performMove(TakePotMove())
            }
            return true
        }
        return false
    }

    override fun meld(move: MeldMove): Boolean {
        if (takenCard) {
            performMove(move)
            if (playerCanTakePot()) {
                performMove(TakePotMove())
            }
            return true
        }
        return false
    }

    override fun getTurnState(): PlayerTurnState {
        return if(!takenCard) PlayerTurnState.PICKING
        else PlayerTurnState.MELDING
    }

    private fun playerCanTakePot() = (state.hand(state.playersTurn).isEmpty() && !playerHasTakenPot())

    private fun playerHasTakenPot() = state.pots[state.playersTurn]?.isEmpty() == true
    
}
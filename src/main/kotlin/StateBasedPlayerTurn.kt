import meld.Meld
import meld.MeldMove
import meld.NewMeldMove
import player.PlayerTurn
import player.PlayerTurnState
import player.PlayerTurnState.*

class StateBasedPlayerTurn(
    private val state: State,
    var takenCard: Boolean = false,
    var discardedCard: Boolean = false,
) : PlayerTurn {

    // todo - check all mutations to state and verify with unit tests for all cases
    override fun performMove(move: Move) {
        val result = move.performMove(state)
        when (move) {
            is TakeCardMove, is TakePileMove -> if (result) takenCard = true
            is DiscardMove -> {
                if (result) discardedCard = true
            }
        }
    }

    fun clone(clonedState: State): StateBasedPlayerTurn {
        return StateBasedPlayerTurn(clonedState, takenCard, discardedCard)
    }

    @Deprecated("Use Move objects instead")
    override fun takeCard(): Boolean {
        if (!takenCard) {
            performMove(TakeCardMove())
            takenCard = true
            return true
        }
        return false
    }

    @Deprecated("Use Move objects instead")
    override fun takePile(): Boolean {
        if (!takenCard) {
            performMove(TakePileMove())
            takenCard = true
            return true
        }
        return false
    }

    @Deprecated("Use Move objects instead")
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

    @Deprecated("Use Move objects instead")
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

    @Deprecated("Use Move objects instead")
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

    @Deprecated("Use Move objects instead")
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
        return if (!takenCard) PICKING
        else if (takenCard && !discardedCard) MELDING
        else FINISHED
    }

    private fun playerCanTakePot() = (state.hand(state.playersTurn).isEmpty() && !playerHasTakenPot())

    private fun playerHasTakenPot() = state.pots[state.playersTurn]?.isEmpty() == true

    override fun roundOver() = takenCard && discardedCard

    override fun equals(other: Any?): Boolean {
        if (other is StateBasedPlayerTurn) {
            return this.takenCard == other.takenCard && this.discardedCard == other.discardedCard
        }
        return super.equals(other)
    }
}
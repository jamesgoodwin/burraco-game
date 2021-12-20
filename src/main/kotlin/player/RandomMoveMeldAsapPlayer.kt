package player

import State
import meld.MeldMove
import java.util.*

class RandomMoveMeldAsapPlayer : Player {

    private val name = UUID.randomUUID()

    override fun takeTurn(state: State, turn: PlayerTurn) {
        var moves = state.getAllPossibleMoves()

        while (moves.isNotEmpty()) {
            val meldMoves = moves.filterIsInstance<MeldMove>()
            val move = meldMoves.firstOrNull() ?: moves.random()
            turn.performMove(move)

            moves = state.getAllPossibleMoves()
        }

        state.printTotalCardCount()
        state.advancePlayer()
    }

    override fun name(): String {
        return "Random Player: $name"
    }

    override fun toString(): String {
        return name()
    }

}
package player

import Move
import State
import ai.ISMCTS
import java.util.*

class IsmctsPlayer : Player {

    private val name = UUID.randomUUID()

    override fun takeTurn(state: State, turn: PlayerTurn) {
        while (state.getAllPossibleMoves().isNotEmpty()) {
            takeAiMove(state, turn)
        }

        state.printTotalCardCount()
        state.advancePlayer()
    }

    private fun takeAiMove(state: State, turn: PlayerTurn) {
        val move = ISMCTS(state.clone(), 20000).run()
        if (move != null) {
            printMove(move)
            turn.performMove(move)
        }
    }

    private fun printMove(takeMove: Move?) {
        println("AI playing move: $takeMove")
    }

    override fun name(): String {
        return "ISMCTS Player: $name"
    }
}
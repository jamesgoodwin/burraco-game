package player

import Move
import State
import ai.ISMCTS
import java.util.*

class IsmctsPlayer : Player {

    private val name = UUID.randomUUID()

    override fun takeTurn(state: State, turn: PlayerTurn) {
        val takeMove = ISMCTS(state.clone(), 1000).run()

        if (takeMove != null) {
            printMove(takeMove)
            turn.performMove(takeMove)
        }

        while (state.getAllPossibleMoves().isNotEmpty()) {
            val move = ISMCTS(state.clone(), 1000).run()
            if (move != null) {
                printMove(takeMove)
                turn.performMove(move)
            }
        }

        state.printTotalCardCount()
        state.advancePlayer()
    }

    private fun printMove(takeMove: Move?) {
        print("AI playing move: $takeMove")
    }

    override fun name(): String {
        return "ISMCTS Player: $name"
    }
}
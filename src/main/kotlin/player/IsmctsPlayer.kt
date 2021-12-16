package player

import State
import ai.ISMCTS
import java.util.*

class IsmctsPlayer : Player {

    private val name = UUID.randomUUID()

    override fun takeTurn(state: State, turn: PlayerTurn) {
        val ismcts = ISMCTS(state.clone(), 25)
        val move = ismcts.run()

        if (move != null) {
            turn.performMove(move)
        }
    }

    override fun name(): String {
        return "ISMCTS Player: $name"
    }
}
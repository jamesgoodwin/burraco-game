package player

import State
import ai.ISMCTS

class IsmctsPlayer : Player {

    override fun takeTurn(state: State, turn: PlayerTurn) {
        val ismcts = ISMCTS(state.copy(), 25)
        val move = ismcts.run()

        if (move != null) {
            turn.performMove(move)
        }
    }

    override fun name(): String {
        return "ISMCTS Player"
    }
}
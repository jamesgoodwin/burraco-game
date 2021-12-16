package ai

import BurracoGame
import State
import meld.MeldMovesFinder
import org.junit.Test
import player.IsmctsPlayer
internal class ISMCTSTest {

    private val players = listOf(IsmctsPlayer(), IsmctsPlayer())

    @Test
    fun shouldSuggestNextMove() {
        val state = State(players, MeldMovesFinder())
        // initialise game
        BurracoGame(state)

        state.takeNextTurn()
    }

}
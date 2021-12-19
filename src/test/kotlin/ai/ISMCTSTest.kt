package ai

import BurracoGame
import State
import meld.MeldMovesFinder
import org.junit.Test
import player.IsmctsPlayer
import kotlin.test.assertNotNull

internal class ISMCTSTest {

    private val players = listOf(IsmctsPlayer(), IsmctsPlayer())

    @Test
    fun shouldSuggestNextMove() {
        val state = State(players, MeldMovesFinder())
        // initialise game
        BurracoGame(state)

        val takeMove = ISMCTS(state, 1000).run()
        assertNotNull(takeMove)
        state.doMove(takeMove)

        val nextMove = ISMCTS(state, 1000).run()
        assertNotNull(nextMove)
    }

}
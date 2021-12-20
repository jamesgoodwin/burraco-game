package ai

import BurracoGame
import State
import meld.MeldMovesFinder
import org.junit.Test
import player.IsmctsPlayer
import kotlin.system.measureTimeMillis
import kotlin.test.assertNotNull

internal class ISMCTSTest {

    private val players = listOf(IsmctsPlayer(), IsmctsPlayer())

    // Took 3352 ms for 1000 simulations
    // Took 11744 ms for 10000 simulations
    @Test
    fun shouldSuggestNextMove() {
        val state = State(players, MeldMovesFinder())
        // initialise game
        BurracoGame(state)

        val time1 = measureTimeMillis {
            val takeMove = ISMCTS(state, 10000).run()
            assertNotNull(takeMove)
            state.doMove(takeMove)
        }

        println("Took $time1 ms for 10000 simulations")

        val time2 = measureTimeMillis {
            val nextMove = ISMCTS(state, 10000).run()
            assertNotNull(nextMove)
        }

        println("Took $time2 ms for 10000 simulations")

    }

}
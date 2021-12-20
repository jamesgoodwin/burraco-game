import meld.MeldMovesFinder
import org.junit.Ignore
import org.junit.Test
import player.IsmctsPlayer
import player.RandomMoveMeldAsapPlayer

class AiVsRandomGameTest {

    @Test
    @Ignore
    fun aiShouldBeAbleToPlayBetterThanRandomPlayer() {
        BurracoGame(State(
            players = listOf(RandomMoveMeldAsapPlayer(), IsmctsPlayer()),
            meldMovesFinder = MeldMovesFinder())).runGame()
    }

}
import org.junit.Assert.assertTrue
import org.junit.Test

internal class BurracoGameTest {

    @Test
    fun shouldHave64CardsInDeck() {
        val game = BurracoGame()

        assertTrue("Deck does not have 64 cards in it", game.state.stock.size == 64)
    }

    @Test
    fun shouldHaveTwoPlayers() {
        val game = BurracoGame()
        assertTrue("Game does not have two players", game.state.players.size == 2)
    }

    @Test
    fun shouldHaveTwoPotsWithElevenCardsEach() {
        val game = BurracoGame()
        val player1 = game.state.players[0]
        val player2 = game.state.players[1]

        assertTrue("First pot does not have 11 cards", game.state.pots[player1]?.size == 11)
        assertTrue("Second pot does not have 11 cards", game.state.pots[player2]?.size == 11)
    }

}
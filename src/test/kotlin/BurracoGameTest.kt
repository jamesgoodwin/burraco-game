import PlayingCard.Suit.DIAMOND
import PlayingCard.Value
import PlayingCard.Value.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.UnsupportedOperationException

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
        assertTrue("First pot does not have 11 cards", game.state.pots[0].size == 11)
        assertTrue("Second pot does not have 11 cards", game.state.pots[1].size == 11)
    }

    @Test
    fun shouldBeAbleToTakeACardPlayerOne() {
        val game = BurracoGame()
        assertTrue(game.state.players[0].hand.size == 11)

        game.state.players[0].takeCard(game)
        assertTrue(game.state.players[0].hand.size == 12)
    }

    @Test
    fun shouldBeAbleToTakeACardAndDiscardACard() {
        val game = BurracoGame()
        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)
    }

    @Test
    fun shouldBeAbleToTakeACardPLayer2AfterPlayer1DiscardsACard() {
        val game = BurracoGame()
        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)

        game.state.players[1].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToTakeACardAfterDiscardingCard() {
        val game = BurracoGame()
        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)
        game.state.players[0].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToTakeACardPlayerTwo() {
        val game = BurracoGame()
        game.state.players[1].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToDiscardACardPlayerTwo() {
        val game = BurracoGame()
        game.state.players[1].discard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToTakeACardMoreThanOnce() {
        val game = BurracoGame()
        game.state.players[0].takeCard(game)
        game.state.players[0].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToDiscardBeforeTaking() {
        val game = BurracoGame()
        game.state.players[0].discard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToDiscardMoreThanOnce() {
        val game = BurracoGame()

        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)
        game.state.players[0].discard(game)
    }

    @Test
    fun shouldBeAbleToPutDownNewMeld() {
        val game = BurracoGame()
        val player1 = game.state.players[0]
        player1.takeCard(game)

        val meld = listOf(
            PlayingCard(Value.ACE, PlayingCard.Suit.SPADE),
            PlayingCard(KING, PlayingCard.Suit.SPADE),
            PlayingCard(Value.QUEEN, PlayingCard.Suit.SPADE)
        )
        // ensure there is a valid meld to put down
        player1.hand.addAll(meld)
        player1.meld(game, meld)
    }

    @Test
    fun shouldRemoveCardsFromHandWhenMeldPlayed() {
        val game = BurracoGame()
        val player1 = game.state.players[0]
        player1.takeCard(game)

        val meld = listOf(
            PlayingCard(JACK, PlayingCard.Suit.SPADE),
            PlayingCard(Value.QUEEN, PlayingCard.Suit.SPADE),
            PlayingCard(KING, PlayingCard.Suit.SPADE)
        )
        // ensure there is a valid meld to put down
        player1.hand.addAll(meld)
        player1.meld(game, meld)

        for (card in meld) {
            @Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
            assertFalse(player1.hand.contains(element = meld))
        }
    }

    @Test
    fun shouldBeAbleToAddCardToExistingMeld() {
        val game = BurracoGame()
        val player1 = game.state.players[0]
        player1.takeCard(game)

        val firstMeld = listOf(
            PlayingCard(JACK, DIAMOND),
            PlayingCard(QUEEN, DIAMOND),
            PlayingCard(KING, DIAMOND)
        )

        player1.meld(game, firstMeld)
        val secondMeld = listOf(PlayingCard(TEN, DIAMOND))
        player1.meld(game, secondMeld, 0)

        assertTrue(player1.melds[0].containsAll(firstMeld + secondMeld))
    }

}
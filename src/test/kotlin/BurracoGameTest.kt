import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.UnsupportedOperationException

internal class BurracoGameTest {

    @Test
    fun shouldHave64CardsInDeck() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        assertTrue("Deck does not have 64 cards in it", game.state.stock.size == 64)
    }

    @Test
    fun shouldHaveTwoPlayers() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        assertTrue("Game does not have two players", game.state.players.size == 2)
    }

    @Test
    fun shouldHaveTwoPotsWithElevenCardsEach() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        assertTrue("First pot does not have 11 cards", game.state.pots[0].size == 11)
        assertTrue("Second pot does not have 11 cards", game.state.pots[1].size == 11)
    }

    @Test
    fun shouldBeAbleToTakeACardPlayerOne() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        assertTrue(game.state.players[0].hand.size == 11)

        game.state.players[0].takeCard(game)
        assertTrue(game.state.players[0].hand.size == 12)
    }

    @Test
    fun shouldBeAbleToTakeACardAndDiscardACard() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)
    }

    @Test
    fun shouldBeAbleToTakeACardPLayer2AfterPlayer1DiscardsACard() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)

        game.state.players[1].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToTakeACardAfterDiscardingCard() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)
        game.state.players[0].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToTakeACardPlayerTwo() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[1].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToDiscardACardPlayerTwo() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[1].discard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToTakeACardMoreThanOnce() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[0].takeCard(game)
        game.state.players[0].takeCard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToDiscardBeforeTaking() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        game.state.players[0].discard(game)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun shouldNotBeAbleToDiscardMoreThanOnce() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)

        game.state.players[0].takeCard(game)
        game.state.players[0].discard(game)
        game.state.players[0].discard(game)
    }

    @Test
    fun shouldBeAbleToPutDownNewMeld() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        val player1 = game.state.players[0]
        player1.takeCard(game)

        val meld = listOf(
            PlayingCard(PlayingCard.Value.ACE, PlayingCard.Suite.SPADES),
            PlayingCard(PlayingCard.Value.KING, PlayingCard.Suite.SPADES),
            PlayingCard(PlayingCard.Value.QUEEN, PlayingCard.Suite.SPADES)
        )
        // ensure there is a valid meld to put down
        player1.hand.addAll(meld)
        player1.meld(game, meld)
    }

    @Test
    fun shouldNotBeAbleToMeldLessThanThreeCards() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        val player1 = game.state.players[0]
        player1.takeCard(game)

        val meld = listOf(
            PlayingCard(PlayingCard.Value.ACE, PlayingCard.Suite.SPADES),
            PlayingCard(PlayingCard.Value.KING, PlayingCard.Suite.SPADES),
        )
        // ensure there is a valid meld to put down
        player1.hand.addAll(meld)
        player1.meld(game, meld)

        assertTrue(player1.melds.isEmpty())
    }

    @Test
    fun shouldRemoveCardsFromHandWhenMeldPlayed() {
        val game = BurracoGame(PlayingMode.TWO_PLAYERS)
        val player1 = game.state.players[0]
        player1.takeCard(game)

        val meld = listOf(
            PlayingCard(PlayingCard.Value.JACK, PlayingCard.Suite.SPADES),
            PlayingCard(PlayingCard.Value.QUEEN, PlayingCard.Suite.SPADES),
            PlayingCard(PlayingCard.Value.KING, PlayingCard.Suite.SPADES)
        )
        // ensure there is a valid meld to put down
        player1.hand.addAll(meld)
        player1.meld(game, meld)

        for(card in meld) {
            assertFalse(player1.hand.contains(meld))
        }
    }

    @Test
    fun shouldBeAbleToAddCardToExistingMeld() {

    }

}
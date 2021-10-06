import PlayingCard.Suit.SPADE
import PlayingCard.Value.*
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFalse

internal class PlayerTurnTest {

    val player1 = HumanPlayer("player 1")
    val player2 = HumanPlayer("player 2")

    @Test
    fun shouldBeAbleToTakeACardPlayerOne() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        assertTrue(state.hand(player1)?.size == 1)
    }

    @Test
    fun shouldBeAbleToTakeACardAndDiscardACard() {
        val state = State(listOf(player1, player2))
        val card = PlayingCard(ACE, SPADE)

        state.stock.add(card)

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        val taken = playerTurn.takeCard()

        val discarded = state.hand(player1)?.get(0)?.let {
            playerTurn.discard(it)
        }

        assertTrue(taken && discarded == true)
    }

    @Test
    fun shouldBeAbleToTakeACardPLayer2AfterPlayer1DiscardsACard() {
        val meldValidator = FastMeldValidator()

        val state = State(listOf(player1, player2))
        state.stock.addAll(listOf(PlayingCard(ACE, SPADE), PlayingCard(ACE, SPADE)))

        val player1Turn = StateBasedPlayerTurn(state, meldValidator)
        player1Turn.takeCard()

        state.hand(player1)?.get(0)?.let {
            player1Turn.discard(it)
        }

        val player2Turn = StateBasedPlayerTurn(state, meldValidator)
        assertTrue(player2Turn.takeCard())
    }

    @Test
    fun shouldNotBeAbleToTakeACardAfterDiscardingCard() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        state.hand(player1)?.get(0)?.let {
            playerTurn.discard(it)
        }

        assertFalse(playerTurn.takeCard())
    }

    @Test
    fun shouldNotBeAbleToTakeACardMoreThanOnce() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        assertFalse(playerTurn.takeCard())
    }

    @Test
    fun shouldNotBeAbleToDiscardBeforeTaking() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        assertFalse(playerTurn.discard(PlayingCard(ACE, SPADE)))
    }

    @Test
    fun shouldNotBeAbleToDiscardMoreThanOnce() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        playerTurn.discard(PlayingCard(ACE, SPADE))
        assertFalse(playerTurn.discard(PlayingCard(KING, SPADE)))
    }

    @Test
    fun shouldBeAbleToPutDownNewMeld() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        playerTurn.discard(PlayingCard(ACE, SPADE))
        assertFalse(playerTurn.discard(PlayingCard(KING, SPADE)))
    }

    @Test
    fun shouldRemoveCardsFromHandWhenMeldPlayed() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val cards = listOf(
            PlayingCard(ACE, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(QUEEN, SPADE)
        )

        state.hands[player1]?.addAll(cards)

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        playerTurn.meld(cards, 0)
        assertTrue(state.hand(player1)?.size == 1)
    }

    @Test
    fun shouldBeAbleToAddCardToExistingMeld() {
        val state = State(listOf(player1, player2))
        state.stock.add(PlayingCard(ACE, SPADE))

        val cards = listOf(
            PlayingCard(ACE, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(QUEEN, SPADE)
        )

        state.hands[player1]?.addAll(cards)

        val playerTurn = StateBasedPlayerTurn(state, FastMeldValidator())
        playerTurn.takeCard()

        playerTurn.meld(cards, 0)
        playerTurn.meld(listOf(PlayingCard(JACK, SPADE)), 0)
    }

}
import PlayingCard.Suit.SPADE
import PlayingCard.Value.*
import meld.MeldMovesFinder
import org.junit.Assert.assertTrue
import org.junit.Test
import player.HumanPlayer
import kotlin.test.assertFalse

internal class PlayerTurnTest {

    private val player1 = HumanPlayer("player 1")
    private val player2 = HumanPlayer("player 2")

    @Test
    fun shouldBeAbleToTakeACardPlayerOne() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        assertTrue(state.hand(player1).size == 1)
    }

    @Test
    fun shouldBeAbleToTakeACardAndDiscardACard() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        val card = PlayingCard(ACE, SPADE)

        state.stock.add(card)

        val playerTurn = StateBasedPlayerTurn(state)
        val taken = playerTurn.takeCard()

        val discarded = state.hand(player1)[0].let {
            playerTurn.discard(it)
        }

        assertTrue(taken && discarded)
    }

    @Test
    fun shouldBeAbleToTakeACardPLayer2AfterPlayer1DiscardsACard() {
        val state = State(listOf(player1, player2), MeldMovesFinder())

        state.stock.addAll(listOf(PlayingCard(ACE, SPADE), PlayingCard(ACE, SPADE)))

        val player1Turn = StateBasedPlayerTurn(state)
        player1Turn.takeCard()

        state.hand(player1)[0].let {
            player1Turn.discard(it)
        }

        val player2Turn = StateBasedPlayerTurn(state)
        assertTrue(player2Turn.takeCard())
    }

    @Test
    fun shouldNotBeAbleToTakeACardAfterDiscardingCard() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        state.hand(player1)[0].let {
            playerTurn.discard(it)
        }

        assertFalse(playerTurn.takeCard())
    }

    @Test
    fun shouldNotBeAbleToTakeACardMoreThanOnce() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        assertFalse(playerTurn.takeCard())
    }

    @Test
    fun shouldNotBeAbleToDiscardBeforeTaking() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state)
        assertFalse(playerTurn.discard(PlayingCard(ACE, SPADE)))
    }

    @Test
    fun shouldNotBeAbleToDiscardMoreThanOnce() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        playerTurn.discard(PlayingCard(ACE, SPADE))
        assertFalse(playerTurn.discard(PlayingCard(KING, SPADE)))
    }

    @Test
    fun shouldBeAbleToPutDownNewMeld() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        playerTurn.discard(PlayingCard(ACE, SPADE))
        assertFalse(playerTurn.discard(PlayingCard(KING, SPADE)))
    }

    @Test
    fun shouldRemoveCardsFromHandWhenMeldPlayed() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val cards = listOf(
            PlayingCard(ACE, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(QUEEN, SPADE)
        )

        state.hands[player1]?.addAll(cards)

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        playerTurn.meld(cards, 0)
        assertTrue(state.hand(player1).size == 1)
    }

    @Test
    fun shouldBeAbleToAddCardToExistingMeld() {
        val state = State(listOf(player1, player2), MeldMovesFinder())
        state.stock.add(PlayingCard(ACE, SPADE))

        val cards = listOf(
            PlayingCard(ACE, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(QUEEN, SPADE)
        )

        state.hands[player1]?.addAll(cards)

        val playerTurn = StateBasedPlayerTurn(state)
        playerTurn.takeCard()

        playerTurn.meld(cards, 0)
        playerTurn.meld(listOf(PlayingCard(JACK, SPADE)), 0)
    }
}
import PlayingCard.Suit.HEART
import PlayingCard.Suit.SPADE
import PlayingCard.Value.*
import meld.MeldMovesFinder
import org.junit.Test
import player.HumanPlayer
import player.Player
import kotlin.test.assertEquals

internal class SeenCardsStateTest {

    private val player1 = HumanPlayer("one")
    private val players = listOf<Player>(player1, HumanPlayer("two"))
    private val meldMovesFinder = MeldMovesFinder()

    @Test
    fun shouldAddToSeenCardsWhenPlayerTakesDiscardPile() {
        val state = State(players, meldMovesFinder)
        val cards = listOf(PlayingCard(ACE, HEART), PlayingCard(TWO, HEART), PlayingCard(THREE, HEART))
        state.discard.addAll(cards)

        state.takePile(player1)
        val seenCards : MutableList<PlayingCard> = state.seenCards[player1] ?: mutableListOf()
        assertEquals(cards, seenCards)
    }

    @Test
    fun shouldRemoveFromSeenCardsWhenCardDiscarded() {
        val state = State(players, meldMovesFinder)
        val aceHearts = PlayingCard(ACE, HEART)
        val otherCards : List<PlayingCard> = listOf(PlayingCard(TWO, HEART), PlayingCard(THREE, HEART))
        val cards = otherCards + aceHearts
        state.seenCards[player1]?.addAll(cards)

        state.discardCard(player1, aceHearts)
        val seenCards : List<PlayingCard> = state.seenCards[player1] ?: mutableListOf()

        assertEquals(otherCards, seenCards)
    }

    @Test
    fun shouldRemoveFromSeenCardsWhenCardsMelded() {
        val state = State(players, meldMovesFinder)
        val aceSpade : List<PlayingCard> = listOf(PlayingCard(ACE, SPADE))
        val meldCards : List<PlayingCard> = listOf(PlayingCard(TWO, HEART), PlayingCard(THREE, HEART), PlayingCard(FOUR, HEART))
        state.hands[player1]?.addAll(meldCards + aceSpade)
        state.seenCards[player1]?.addAll(meldCards + aceSpade)

        state.meld(player1, meldCards, true)
        val seenCards : List<PlayingCard> = state.seenCards[player1] ?: mutableListOf()
        assertEquals(aceSpade, seenCards)
    }

}
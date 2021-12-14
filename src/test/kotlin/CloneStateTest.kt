import PlayingCard.Suit.HEART
import PlayingCard.Suit.SPADE
import PlayingCard.Value.JOKER
import PlayingCard.Value.TWO
import meld.MeldMovesFinder
import org.junit.Test
import player.HumanPlayer
import player.Player
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

internal class CloneStateTest {

    private val players = listOf<Player>(HumanPlayer("one"), HumanPlayer("two"))
    private val meldMovesFinder = MeldMovesFinder()

    @Test
    fun shouldClonePlayers() {
        val state = State(players, meldMovesFinder)
        val clone = state.clone()

        assertEquals(state.players, clone.players)
    }

    @Test
    fun shouldClonePlayersTurn() {
        val state = State(players, meldMovesFinder)
        val clone = state.clone()

        assertEquals(state.playersTurn, clone.playersTurn)
    }

    @Test
    fun shouldCloneDiscardedCards() {
        val state = State(players, meldMovesFinder)
        state.discard.add(PlayingCard(JOKER))
        val clone = state.clone()

        assertEquals(state.discard, clone.discard)
        assertNotSame(state.discard, clone.discard)
    }

    @Test
    fun shouldClonePots() {
        val state = State(players, meldMovesFinder)
        state.pots[state.players[0]]?.add(PlayingCard(TWO, HEART))
        state.pots[state.players[1]]?.add(PlayingCard(TWO, SPADE))

        val clone = state.clone()
        assertEquals(state.pots, clone.pots)
        assertNotSame(state.pots, clone.pots)
    }

    @Test
    fun shouldCloneMelds() {
        val state = State(players, meldMovesFinder)
        state.melds[state.players[0]]?.add(mutableListOf(PlayingCard(TWO, HEART)))
        state.melds[state.players[1]]?.add(mutableListOf(PlayingCard(TWO, SPADE)))

        val clone = state.clone()
        assertEquals(state.melds, clone.melds)
        assertNotSame(state.melds, clone.melds)
    }

    @Test
    fun shouldCloneHands() {
        val state = State(players, meldMovesFinder)
        state.hands[state.players[0]]?.addAll(mutableListOf(PlayingCard(TWO, HEART)))
        state.hands[state.players[1]]?.addAll(mutableListOf(PlayingCard(TWO, SPADE)))

        val clone = state.clone()
        assertEquals(state.hands, clone.hands)
        assertNotSame(state.hands, clone.hands)
    }

}
import PlayingCard.Suit.HEART
import PlayingCard.Suit.SPADE
import PlayingCard.Value.*
import meld.MeldMovesFinder
import org.junit.Test
import player.HumanPlayer
import player.Player
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

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

    @Test
    fun shouldCloneSeenCards() {
        val state = State(players, meldMovesFinder)
        state.seenCards[players.first()]?.addAll(listOf(PlayingCard(ACE, SPADE)))

        val clone = state.clone()
        assertEquals(state.seenCards, clone.seenCards)
        assertNotSame(state.seenCards, clone.seenCards)
    }

    @Test
    fun shouldCloneAndRandomiseStateOfHiddenCards() {
        val state = State(players, meldMovesFinder)
        BurracoGame(state)

        val firstPlayer = players.first()
        val threeCards = state.hands[firstPlayer]?.take(3) ?: emptyList()
        state.seenCards[firstPlayer]?.addAll(threeCards)

        val clone = state.cloneAndRandomiseState()

        assertTrue(clone.hand(firstPlayer).containsAll(threeCards))

        assertNotEquals(state, clone)
        assertNotEquals(state.hands, clone.hands)
        assertNotEquals(state.stock, clone.stock)
    }

}
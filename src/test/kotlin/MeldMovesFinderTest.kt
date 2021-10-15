import PlayingCard.Suit.DIAMOND
import PlayingCard.Suit.HEART
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class MeldMovesFinderTest {

    @Test
    fun shouldMeldNew() {
        val meldMovesFinder = MeldMovesFinder(FastMeldValidator())
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(PlayingCard.Value.SEVEN, HEART),
            PlayingCard(PlayingCard.Value.EIGHT, HEART)
        )
        val melds = emptyList<Meld>()
        val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
        val moves = meldMovesFinder.findMoves(hand, state, melds)
        if (moves != null) {
            assertTrue(moves.isNotEmpty())
        }
    }

    @Test
    fun shouldMeldToExisting() {
        val meldMovesFinder = MeldMovesFinder(FastMeldValidator())
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(PlayingCard.Value.EIGHT, HEART),
            PlayingCard(PlayingCard.Value.SIX, DIAMOND)
        )
        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(PlayingCard.Value.THREE, HEART),
                    PlayingCard(PlayingCard.Value.FOUR, HEART),
                    PlayingCard(PlayingCard.Value.FIVE, HEART)
                )
            ),
            Meld(
                listOf(
                    PlayingCard(PlayingCard.Value.NINE, HEART),
                    PlayingCard(PlayingCard.Value.TEN, HEART),
                    PlayingCard(PlayingCard.Value.JACK, HEART)
                )
            )
        )
        val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
        val moves = meldMovesFinder.findMoves(hand, state, melds)
        assertNotNull(moves)
    }

}


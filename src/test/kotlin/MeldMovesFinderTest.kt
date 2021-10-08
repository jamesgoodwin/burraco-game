import PlayingCard.Suit.DIAMOND
import PlayingCard.Suit.HEART
import org.junit.Test
import kotlin.test.assertNotNull

internal class MeldMovesFinderTest {

    @Test
    fun shouldMeldToExisting() {
        val meldMovesFinder = MeldMovesFinder(FastMeldValidator())
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(PlayingCard.Value.EIGHT, HEART),
            PlayingCard(PlayingCard.Value.SIX, DIAMOND)
        )
        val melds = mapOf(
            HEART to listOf(
                PlayingCard(PlayingCard.Value.THREE, HEART),
                PlayingCard(PlayingCard.Value.FOUR, HEART),
                PlayingCard(PlayingCard.Value.FIVE, HEART)
            )
        )
        val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
        assertNotNull(meldMovesFinder.findMoves(hand, state, melds))
    }

}


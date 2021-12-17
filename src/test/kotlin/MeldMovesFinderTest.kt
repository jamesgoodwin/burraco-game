import org.junit.Ignore
import org.junit.Test
import PlayingCard.Suit.*
import PlayingCard.Value.*
import meld.Meld
import meld.MeldMovesFinder
import player.HumanPlayer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MeldMovesFinderTest {

    private val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")), MeldMovesFinder())
    private val meldMovesFinder = MeldMovesFinder()

    @Test
    fun shouldMeldMultipleNewSequencesAndCombinations() {
        val hand = listOf(
            PlayingCard(QUEEN, HEART),
            PlayingCard(ACE, HEART),
            PlayingCard(TWO, DIAMOND),
            PlayingCard(FIVE, DIAMOND),
            PlayingCard(FIVE, CLUB),
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(EIGHT, SPADE),
            PlayingCard(EIGHT, SPADE),
            PlayingCard(NINE, SPADE),
            PlayingCard(ACE, SPADE)
        )

        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(TWO, HEART),
                    PlayingCard(EIGHT, CLUB),
                    PlayingCard(NINE, CLUB),
                )
            ),
            Meld(
                listOf(
                    PlayingCard(KING, HEART),
                    PlayingCard(KING, DIAMOND),
                    PlayingCard(KING, CLUB),
                )
            ),
            Meld(
                listOf(
                    PlayingCard(SIX, HEART),
                    PlayingCard(SIX, DIAMOND),
                    PlayingCard(SIX, CLUB),
                )
            )
        )

        val moves = meldMovesFinder.getAllMoves(hand, state, melds)

        //new melds expected Q,2,A - H, 5,5,2, 3,4,2, 8,8,2, 8,9,2,
        // also adding 2 to existing melds KKK and 666
        assertEquals(5, moves.size)
    }

}
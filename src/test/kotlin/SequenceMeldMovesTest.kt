import org.junit.Ignore
import org.junit.Test
import PlayingCard.Suit.*
import PlayingCard.Value.*
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class SequenceMeldMovesTest {

    private val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
    private val meldMovesFinder = MeldMovesFinder()

    @Test
    fun shouldMeldNewSequence() {
        val hand = listOf(
            PlayingCard(SIX, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(EIGHT, HEART)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertTrue(moves.isNotEmpty())
    }

    @Test
    fun shouldMeldNewSequenceWhenJokerFirst() {
        val hand = listOf(
            PlayingCard(JOKER),
            PlayingCard(EIGHT, DIAMOND),
            PlayingCard(TEN, DIAMOND),
            PlayingCard(KING, DIAMOND),
            PlayingCard(ACE, DIAMOND)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertTrue(moves.isNotEmpty())
    }

    @Test
    @Ignore("will work on this after individual tests are working for 4 meld types")
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
        assertTrue(moves.size == 5)
    }

    @Test
    fun shouldMeldNewSequenceWithWildcard() {
        val hand = listOf(
            PlayingCard(SIX, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(PlayingCard.Value.TWO, SPADE)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertTrue(moves.isNotEmpty())
    }


    @Test
    fun shouldMeldToExistingSequence() {
        val hand = listOf(
            PlayingCard(SIX, HEART),
            PlayingCard(EIGHT, HEART),
            PlayingCard(SIX, DIAMOND)
        )
        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(THREE, HEART),
                    PlayingCard(FOUR, HEART),
                    PlayingCard(FIVE, HEART)
                )
            ),
            Meld(
                listOf(
                    PlayingCard(NINE, HEART),
                    PlayingCard(TEN, HEART),
                    PlayingCard(JACK, HEART)
                )
            )
        )
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertNotNull(moves)
    }

}
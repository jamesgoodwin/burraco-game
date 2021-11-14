import PlayingCard.Suit.*
import PlayingCard.Value.*
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class MeldMovesFinderTest {

    private val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
    private val meldMovesFinder = MeldMovesFinder()

    @Test
    fun shouldMeldNewSequence() {
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(PlayingCard.Value.EIGHT, HEART)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertTrue(moves.isNotEmpty())
    }

    @Test
    fun shouldMeldNewSequenceWhenJokerFirst() {
        val hand = listOf(
            PlayingCard(PlayingCard.Value.JOKER),
            PlayingCard(PlayingCard.Value.EIGHT, DIAMOND),
            PlayingCard(PlayingCard.Value.TEN, DIAMOND),
            PlayingCard(PlayingCard.Value.KING, DIAMOND),
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

//    Hand: 10♥, 8♦, 9♣, 2♠
//    Melds: 2♥, 10♥, 10♣, 10♠ - 3♥, 3♣, 3♣ - J♥, J♦, J♣ - 8♦, 8♣, 8♠

    @Test
    fun shouldMeldNewSequenceWithWildcard() {
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(PlayingCard.Value.TWO, SPADE)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertTrue(moves.isNotEmpty())
    }

    @Test
    fun shouldMeldNewCombination() {
        val hand = mapOf(
            SEVEN to
                    listOf(
                        PlayingCard(SEVEN, HEART),
                        PlayingCard(SEVEN, HEART),
                        PlayingCard(SEVEN, SPADE)
                    )
        )
        val moves = meldMovesFinder.getNewCombinationMeldMoves(hand, emptyList(), state)
        assertTrue(moves.isNotEmpty())
    }

    @Test
    fun shouldMeldToExistingCombination() {
        val hand = listOf(PlayingCard(THREE, DIAMOND))

        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(THREE, HEART),
                    PlayingCard(THREE, SPADE),
                    PlayingCard(THREE, HEART)
                )
            )
        )
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertNotNull(moves)
    }

    //test - 10♥, Q♥, K♥, K♥, 8♦, 10♦, 4♣, 5♣, 5♣, 7♣, 9♣, 4♠, Q♠, Q♠, K♠

//    T: 108, H: 17, M: 9, P:22, S:60, D:0
//    Hand: 10♥, Q♥, K♥, K♥, 8♦, 10♦, 4♣, 5♣, 5♣, 7♣, 9♣, 4♠
//    Melds: Q♠, Q♠, K♠
//    Enter a card to discard

    @Test
    fun shouldMeldToExisting() {
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(PlayingCard.Value.EIGHT, HEART),
            PlayingCard(PlayingCard.Value.SIX, DIAMOND)
        )
        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(THREE, HEART),
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
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertNotNull(moves)
    }

}
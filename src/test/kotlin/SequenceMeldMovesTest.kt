import org.junit.Test
import PlayingCard.Suit.*
import PlayingCard.Value.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class SequenceMeldMovesTest {

    private val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
    private val meldMovesFinder = MeldMovesFinder()

//    Hand: Joker, Joker, 5♥, 7♥, J♥, 3♦, 4♦, 8♦, 4♠, Q♠

//    Hand: 3♥, 4♥, 5♥, J♥, Q♣, 8♠, 9♠, Q♠
//    Melds: 2♥, 10♥, 10♣, 10♠ - A♥, A♦, A♠, A♠ - 6♣, 7♣, 8♣

    @Test
    fun shouldMeldThreeFourFiveHearts() {
        val hand = listOf(
            PlayingCard(THREE, HEART),
            PlayingCard(FOUR, HEART),
            PlayingCard(FIVE, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(QUEEN, CLUB),
            PlayingCard(EIGHT, SPADE),
            PlayingCard(NINE, SPADE),
            PlayingCard(QUEEN, SPADE),
        )

        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(TWO, HEART),
                    PlayingCard(TEN, HEART),
                    PlayingCard(TEN, CLUB),
                    PlayingCard(TEN, SPADE)
                )
            ),
            Meld(
                listOf(
                    PlayingCard(ACE, HEART),
                    PlayingCard(ACE, DIAMOND),
                    PlayingCard(ACE, SPADE),
                    PlayingCard(ACE, SPADE)
                )
            ),
            Meld(listOf(PlayingCard(SIX, CLUB), PlayingCard(SEVEN, CLUB), PlayingCard(EIGHT, CLUB)))
        )
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertEquals(moves.size, 1)
    }

    @Test
    fun shouldMeldFourCardSequenceWithJoker() {
//        Joker, 4♥, 5♥, 7♥, J♥, Q♥, 6♦, 9♦, K♦, 4♠, 7♠, 8♠
        val hand = listOf(
            PlayingCard(JOKER),
            PlayingCard(FOUR, HEART),
            PlayingCard(FIVE, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(QUEEN, HEART),
        )

        val moves = meldMovesFinder.getAllMoves(hand, state, emptyList())
        assertEquals(moves.size, 4)
    }

    @Test
    fun shouldMeldNewSequenceMultipleJokers() {
        val hand = listOf(
            PlayingCard(JOKER),
            PlayingCard(JOKER),
            PlayingCard(FIVE, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(THREE, DIAMOND),
            PlayingCard(FOUR, DIAMOND),
            PlayingCard(EIGHT, DIAMOND),
            PlayingCard(FOUR, CLUB),
            PlayingCard(QUEEN, SPADE),
        )
        val moves = meldMovesFinder.getAllMoves(hand, state, emptyList())
        assertEquals(6, moves.size)
    }

//    3♥, 5♥, J♥, Q♥, 8♦, 3♣, 5♠, Q♠, K♠, A♠

    @Test
    fun shouldMeldQueenKingAceSequence() {
        val hand = listOf(
            PlayingCard(THREE, HEART),
            PlayingCard(FOUR, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(QUEEN, HEART),
            PlayingCard(EIGHT, DIAMOND),
            PlayingCard(THREE, CLUB),
            PlayingCard(FIVE, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(ACE, SPADE),
        )

        val moves = meldMovesFinder.getAllMoves(hand, state, emptyList())
        assertEquals(moves.size, 1)
    }

    @Test
    fun shouldMeldNewSequence() {
        val hand = mapOf<PlayingCard.Suit?, List<PlayingCard>>(
            HEART to listOf(
                PlayingCard(SIX, HEART),
                PlayingCard(SEVEN, HEART),
                PlayingCard(EIGHT, HEART)
            )
        )
        val moves = meldMovesFinder.getNewSequenceMeldMoves(hand, emptyList(), state)
        assertEquals(moves.size, 1)
    }

    @Test
    fun shouldMeldNewSequenceWithTwoInMiddle() {
        val hand = listOf(
            PlayingCard(SIX, HEART),
            PlayingCard(TWO, SPADE),
            PlayingCard(EIGHT, HEART)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertEquals(moves.size, 1)
    }

    // todo test for 2. Add Joker to existing meld: Q♥,A♥,K♥,2♦

    // todo test for Add ten to existing meld - Hand: 10♥, Q♥, K♣, A♠
    //Melds: Q♥, K♥, A♥, 2♦

    @Test
    fun shouldAddToExistingMeldWithWildcard() {
        val hand = listOf(
            PlayingCard(TEN, HEART),
            PlayingCard(QUEEN, HEART)
        )
        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(QUEEN, HEART),
                    PlayingCard(KING, HEART),
                    PlayingCard(ACE, HEART),
                    PlayingCard(TWO, DIAMOND)
                )
            )
        )
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertEquals(1, moves.size)
    }

    @Test
    fun shouldMeldNewSequenceWithJokerAtEnd() {
        val hand = listOf(
            PlayingCard(JOKER),
            PlayingCard(THREE, DIAMOND),
            PlayingCard(FOUR, DIAMOND)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertEquals(1, moves.size)
    }

    @Test
    fun shouldMeldNewSequenceWithJokerInMiddle() {
        val hand = listOf(
            PlayingCard(SIX, HEART),
            PlayingCard(JOKER),
            PlayingCard(EIGHT, HEART)
        )
        val melds = emptyList<Meld>()
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertEquals(1, moves.size)
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
    fun shouldMeldNewSequenceWithWildcard() {
        val hand = listOf(
            PlayingCard(SIX, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(TWO, SPADE)
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

    @Test
    fun shouldMeldWildcardToExistingSequence() {
        val twoOfDiamonds = PlayingCard(TWO, DIAMOND)
        val hand = mapOf<PlayingCard.Suit?, List<PlayingCard>>(
            DIAMOND to listOf(
                twoOfDiamonds
            )
        )

        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(NINE, HEART),
                    PlayingCard(TEN, HEART),
                    PlayingCard(JACK, HEART)
                )
            )
        )
        val moves = meldMovesFinder.getMeldToExistingSequenceMoves(hand, listOf(twoOfDiamonds), melds, state)
        assertNotNull(moves)
    }

}
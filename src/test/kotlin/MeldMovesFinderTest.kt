import PlayingCard.Suit.DIAMOND
import PlayingCard.Suit.HEART
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class MeldMovesFinderTest {

    @Test
    fun shouldMeldNew() {
        val meldMovesFinder = MeldMovesFinder()
        val hand = listOf(
            PlayingCard(PlayingCard.Value.SIX, HEART),
            PlayingCard(PlayingCard.Value.SEVEN, HEART),
            PlayingCard(PlayingCard.Value.EIGHT, HEART)
        )
        val melds = emptyList<Meld>()
        val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
        val moves = meldMovesFinder.findMoves(hand, state, melds)
        assertTrue(moves.isNotEmpty())
    }

    //test - 10♥, Q♥, K♥, K♥, 8♦, 10♦, 4♣, 5♣, 5♣, 7♣, 9♣, 4♠, Q♠, Q♠, K♠

//    T: 108, H: 17, M: 9, P:22, S:60, D:0
//    Hand: 10♥, Q♥, K♥, K♥, 8♦, 10♦, 4♣, 5♣, 5♣, 7♣, 9♣, 4♠
//    Melds: Q♠, Q♠, K♠
//    Enter a card to discard

    @Test
    fun shouldMeldToExisting() {
        val meldMovesFinder = MeldMovesFinder()
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


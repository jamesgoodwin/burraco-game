import PlayingCard.Suit.*
import PlayingCard.Value.*
import meld.Meld
import meld.MeldMovesFinder
import org.junit.Test
import player.HumanPlayer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CombinationsMeldMovesTest {

    private val state = State(listOf(HumanPlayer("bob"), HumanPlayer("sue")))
    private val meldMovesFinder = MeldMovesFinder()

//    Hand: 10♥, 8♦, 9♣, 2♠
//    Melds: 2♥, 10♥, 10♣, 10♠ - 3♥, 3♣, 3♣ - J♥, J♦, J♣ - 8♦, 8♣, 8♠

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
    fun shouldMeldNewCombinationWithWildcard() {
        val twoOfDiamonds = PlayingCard(TWO, DIAMOND)
        val hand = mapOf(
            SEVEN to
                    listOf(
                        PlayingCard(SEVEN, HEART),
                        PlayingCard(SEVEN, HEART),
                        PlayingCard(SEVEN, SPADE)
                    ),
            TWO to listOf(twoOfDiamonds)
        )
        val moves = meldMovesFinder.getNewCombinationMeldMoves(hand, listOf(twoOfDiamonds), state)
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
        assertEquals(moves.size, 1)
    }

    @Test
    fun shouldMeldJokerToExistingCombination() {
        val hand = listOf(PlayingCard(JOKER))

        val melds = listOf(
            Meld(
                listOf(
                    PlayingCard(NINE, DIAMOND),
                    PlayingCard(NINE, CLUB),
                    PlayingCard(NINE, CLUB)
                )
            )
        )
        val moves = meldMovesFinder.getAllMoves(hand, state, melds)
        assertEquals(1, moves.size)
    }

    //test - 10♥, Q♥, K♥, K♥, 8♦, 10♦, 4♣, 5♣, 5♣, 7♣, 9♣, 4♠, Q♠, Q♠, K♠

//    T: 108, H: 17, M: 9, P:22, S:60, D:0
//    Hand: 10♥, Q♥, K♥, K♥, 8♦, 10♦, 4♣, 5♣, 5♣, 7♣, 9♣, 4♠
//    Melds: Q♠, Q♠, K♠
//    Enter a card to discard

}
import PlayingCard.Suit.DIAMOND
import PlayingCard.Suit.HEART
import PlayingCard.Value.*
import org.junit.Test
import kotlin.test.assertEquals

internal class MeldCardOrderingTest {

    // todo - test for jolly should be low if it can be high or low

    @Test
    fun shouldOrderAceAfterKing() {
        val jackOfHearts = PlayingCard(JACK, HEART)
        val queenOfHearts = PlayingCard(QUEEN, HEART)
        val kingOfHearts = PlayingCard(KING, HEART)
        val aceOfHearts = PlayingCard(ACE, HEART)

        val meld = Meld(listOf(aceOfHearts, jackOfHearts, queenOfHearts, kingOfHearts))

        assertEquals(jackOfHearts, meld.cardsOrdered[0])
        assertEquals(queenOfHearts, meld.cardsOrdered[1])
        assertEquals(kingOfHearts, meld.cardsOrdered[2])
        assertEquals(aceOfHearts, meld.cardsOrdered[3])
    }

    @Test
    fun shouldOrderAceAndTwoBeforeThree() {
        val aceOfHearts = PlayingCard(ACE, HEART)
        val threeOfHearts = PlayingCard(THREE, HEART)
        val fourOfHearts = PlayingCard(FOUR, HEART)
        val twoOfHearts = PlayingCard(TWO, HEART)

        val meld = Meld(listOf(aceOfHearts, threeOfHearts, fourOfHearts, twoOfHearts))

        assertEquals(aceOfHearts, meld.cardsOrdered[0])
        assertEquals(twoOfHearts, meld.cardsOrdered[1])
        assertEquals(threeOfHearts, meld.cardsOrdered[2])
        assertEquals(fourOfHearts, meld.cardsOrdered[3])
    }

    @Test
    fun shouldOrderByNaturalOrder() {
        val fiveOfHearts = PlayingCard(FIVE, HEART)
        val fourOfHearts = PlayingCard(FOUR, HEART)
        val threeOfHearts = PlayingCard(THREE, HEART)

        val meld = Meld(listOf(fiveOfHearts, fourOfHearts, threeOfHearts))

        assertEquals(threeOfHearts, meld.cardsOrdered[0])
        assertEquals(fourOfHearts, meld.cardsOrdered[1])
        assertEquals(fiveOfHearts, meld.cardsOrdered[2])
    }

    @Test
    fun shouldOrderByNaturalOrder2() {
        val eightOfHearts = PlayingCard(EIGHT, HEART)
        val sixOfHearts = PlayingCard(SIX, HEART)
        val sevenOfHearts = PlayingCard(SEVEN, HEART)

        val meld = Meld(listOf(eightOfHearts, sixOfHearts, sevenOfHearts))

        assertEquals(sixOfHearts, meld.cardsOrdered[0])
        assertEquals(sevenOfHearts, meld.cardsOrdered[1])
        assertEquals(eightOfHearts, meld.cardsOrdered[2])
    }

    @Test
    fun shouldOrderWildcardInMiddle() {
        val fiveOfHearts = PlayingCard(FIVE, HEART)
        val threeOfHearts = PlayingCard(THREE, HEART)
        val twoOfDiamonds = PlayingCard(TWO, DIAMOND)

        val meld = Meld(listOf(fiveOfHearts, threeOfHearts, twoOfDiamonds))

        assertEquals(threeOfHearts, meld.cardsOrdered[0])
        assertEquals(twoOfDiamonds, meld.cardsOrdered[1])
        assertEquals(fiveOfHearts, meld.cardsOrdered[2])
    }

    @Test
    fun shouldOrderWildcardInMiddleWithNaturalTwo() {
        val threeOfHearts = PlayingCard(THREE, HEART)
        val twoOfHearts = PlayingCard(TWO, HEART)
        val fiveOfHearts = PlayingCard(FIVE, HEART)
        val twoOfDiamonds = PlayingCard(TWO, DIAMOND)

        val meld = Meld(listOf(threeOfHearts, fiveOfHearts, twoOfHearts, twoOfDiamonds))

        assertEquals(twoOfHearts, meld.cardsOrdered[0])
        assertEquals(threeOfHearts, meld.cardsOrdered[1])
        assertEquals(twoOfDiamonds, meld.cardsOrdered[2])
        assertEquals(fiveOfHearts, meld.cardsOrdered[3])
    }

}
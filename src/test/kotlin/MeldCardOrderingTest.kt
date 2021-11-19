import PlayingCard.Suit.DIAMOND
import PlayingCard.Suit.HEART
import PlayingCard.Value.*
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

internal class MeldCardOrderingTest {

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
    @Ignore
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
import PlayingCard.Suit.*
import PlayingCard.Value.*
import org.junit.Test
import kotlin.test.*

internal class MeldTest {

    @Test
    fun isAllSameSuite() {
        val meld = Meld(
            listOf(
                PlayingCard(ACE, SPADE),
                PlayingCard(TWO, SPADE),
                PlayingCard(THREE, SPADE)
            )
        )
        assertNotNull(meld.suit)
        assertNull(meld.value)
        assertEquals(meld.suit, SPADE)
    }

    @Test
    fun isValidWithWildcardInMiddle() {
        val meld = Meld(
            listOf(
                PlayingCard(SIX, SPADE),
                PlayingCard(TWO, HEART),
                PlayingCard(EIGHT, SPADE)
            )
        )
        assertTrue(meld.valid)
    }

    @Test
    fun isAllSameSuiteWithWildcard() {
        val meld = Meld(
            listOf(
                PlayingCard(ACE, SPADE),
                PlayingCard(TWO, SPADE),
                PlayingCard(TWO, HEART)
            )
        )

        assertNotNull(meld.suit)
        assertNull(meld.value)
        assertEquals(meld.suit, SPADE)
    }

    @Test
    fun isAllSameValue() {
        val meld = Meld(
            listOf(
                PlayingCard(FIVE, HEART),
                PlayingCard(FIVE, SPADE),
                PlayingCard(FIVE, DIAMOND)
            )
        )
        assertNotNull(meld.value)
        assertNull(meld.suit)
    }

    @Test
    fun isAllSameValueWithWildcard() {
        val meld = Meld(
            listOf(
                PlayingCard(FIVE, HEART),
                PlayingCard(FIVE, SPADE),
                PlayingCard(TWO, DIAMOND)
            )
        )
        assertNotNull(meld.value)
        assertNull(meld.suit)
    }

//    Choose cards to meld
//    1. Meld 3♣,3♣,5♣,6♣,7♣
//    2. Meld 3♣,3♣,5♣,6♣
//    3. Meld 3♣,3♣,5♣
//    4. Meld 5♣,6♣,7♣

    @Test
    fun isAllSameValueWithJokerWildcard() {
        val meld = Meld(
            listOf(
                PlayingCard(FIVE, HEART),
                PlayingCard(FIVE, SPADE),
                PlayingCard(JOKER, DIAMOND)
            )
        )
        assertNotNull(meld.value)
        assertNull(meld.suit)
    }

    @Test
    fun isNotValidWithTwoWildcards() {
        val meld = Meld(
            listOf(
                PlayingCard(JOKER),
                PlayingCard(NINE, DIAMOND),
                PlayingCard(JACK, DIAMOND),
                PlayingCard(TWO, CLUB)
            )
        )
        assertFalse(meld.valid)
    }

}
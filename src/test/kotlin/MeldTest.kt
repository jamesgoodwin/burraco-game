import PlayingCard.Suit.*
import PlayingCard.Value.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        assertTrue(meld.isAllSameSuite())
        assertEquals(meld.suite(), SPADE)
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
        assertTrue(meld.isAllSameSuite())
        assertFalse(meld.isSameValue())
        assertEquals(meld.suite(), SPADE)
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
        assertTrue(meld.isSameValue())
        assertFalse(meld.isAllSameSuite())
        assertNull(meld.suite())
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
        assertTrue(meld.isSameValue())
        assertFalse(meld.isAllSameSuite())
        assertNull(meld.suite())
    }

    @Test
    fun isAllSameValueWithJokerWildcard() {
        val meld = Meld(
            listOf(
                PlayingCard(FIVE, HEART),
                PlayingCard(FIVE, SPADE),
                PlayingCard(JOKER, DIAMOND)
            )
        )
        assertTrue(meld.isSameValue())
        assertFalse(meld.isAllSameSuite())
        assertNull(meld.suite())
    }
}
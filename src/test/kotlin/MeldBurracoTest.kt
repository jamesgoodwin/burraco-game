import PlayingCard.Suit.HEART
import PlayingCard.Value.*
import meld.Burraco
import meld.Meld
import org.junit.Test
import kotlin.test.assertEquals

internal class MeldBurracoTest {

    @Test
    fun shouldBeCleanBurraco() {
        val meld = Meld(listOf(
            PlayingCard(SEVEN, HEART),
            PlayingCard(EIGHT, HEART),
            PlayingCard(NINE, HEART),
            PlayingCard(TEN, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(QUEEN, HEART),
            PlayingCard(KING, HEART),
        ))
        assertEquals(Burraco.PULITO, meld.getBurracoType())
    }

    @Test
    fun shouldBeDirtyBurraco() {
        val meld = Meld(listOf(
            PlayingCard(SEVEN, HEART),
            PlayingCard(EIGHT, HEART),
            PlayingCard(NINE, HEART),
            PlayingCard(TWO, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(QUEEN, HEART),
            PlayingCard(KING, HEART),
        ))
        assertEquals(Burraco.SPORCO, meld.getBurracoType())
    }

    @Test
    fun shouldBeNoBurraco() {
        val meld = Meld(listOf(
            PlayingCard(SEVEN, HEART),
            PlayingCard(EIGHT, HEART),
            PlayingCard(NINE, HEART),
            PlayingCard(TWO, HEART)
        ))
        assertEquals(Burraco.NO_BURRACO, meld.getBurracoType())
    }

    @Test
    fun shouldBeSemiCleanBurraco() {
        val meld = Meld(listOf(
            PlayingCard(TWO, HEART),
            PlayingCard(SEVEN, HEART),
            PlayingCard(EIGHT, HEART),
            PlayingCard(NINE, HEART),
            PlayingCard(TEN, HEART),
            PlayingCard(JACK, HEART),
            PlayingCard(QUEEN, HEART),
            PlayingCard(KING, HEART),
        ))
        assertEquals(Burraco.SEMI_PULITO, meld.getBurracoType())
    }


}
import PlayingCard.Suit.*
import PlayingCard.Value.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class SlowMeldValidatorTest {

    @Test
    fun shouldBeAbleToMeldFullSuiteOfCards() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(ACE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SIX, SPADE),
            PlayingCard(SEVEN, SPADE),
            PlayingCard(EIGHT, SPADE),
            PlayingCard(NINE, SPADE),
            PlayingCard(TEN, SPADE),
            PlayingCard(JACK, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(ACE, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeStraightCards() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(PlayingCard(JACK, SPADE), PlayingCard(QUEEN, SPADE), PlayingCard(KING, SPADE))

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwo() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(TWO, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndAnAce() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(ACE, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTenThroughToKing() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(JACK, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(TEN, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwoInTheMiddle() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(FIVE, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeNonConsecutiveCards() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SEVEN, SPADE)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeConsecutiveCardsDifferentSuites() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, CLUB),
            PlayingCard(FIVE, SPADE)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoCards() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldFourStraightCards() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SIX, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoTwosSameSuite() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(TWO, SPADE)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoWildCards() {
        val meldValidator = SlowMeldValidator()
        val meld = listOf(PlayingCard(JOKER, SPADE), PlayingCard(TWO, SPADE), PlayingCard(FIVE, SPADE))

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoIdenticalCardsInStraight() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(FIVE, SPADE)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue() {
        val meldValidator = SlowMeldValidator()

        val meld = listOf(PlayingCard(KING, HEART), PlayingCard(KING, SPADE), PlayingCard(KING, DIAMOND))
        assertTrue(meldValidator.isValidMeld(meld))
    }

}
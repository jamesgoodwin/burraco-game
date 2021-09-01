import PlayingCard.Suite.*
import PlayingCard.Value.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class MeldValidatorTest {

    @Test
    fun shouldBeAbleToMeldFullSuiteOfCards() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(ACE, SPADES),
            PlayingCard(TWO, SPADES),
            PlayingCard(THREE, SPADES),
            PlayingCard(FOUR, SPADES),
            PlayingCard(FIVE, SPADES),
            PlayingCard(SIX, SPADES),
            PlayingCard(SEVEN, SPADES),
            PlayingCard(EIGHT, SPADES),
            PlayingCard(NINE, SPADES),
            PlayingCard(TEN, SPADES),
            PlayingCard(JACK, SPADES),
            PlayingCard(QUEEN, SPADES),
            PlayingCard(KING, SPADES),
            PlayingCard(ACE, SPADES)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeStraightCards() {
        val meldValidator = MeldValidator()

        val meld = listOf(PlayingCard(JACK, SPADES), PlayingCard(QUEEN, SPADES), PlayingCard(KING, SPADES))

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwo() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADES),
            PlayingCard(KING, SPADES),
            PlayingCard(TWO, SPADES)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndAnAce() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADES),
            PlayingCard(KING, SPADES),
            PlayingCard(ACE, SPADES)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwoInTheMiddle() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(TWO, SPADES),
            PlayingCard(FIVE, SPADES)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeNonConsecutiveCards() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(FIVE, SPADES),
            PlayingCard(SEVEN, SPADES)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeConsecutiveCardsDifferentSuites() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(FOUR, CLUB),
            PlayingCard(FIVE, SPADES)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoCards() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(FOUR, SPADES)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldFourStraightCards() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(FOUR, SPADES),
            PlayingCard(FIVE, SPADES),
            PlayingCard(SIX, SPADES)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoTwosSameSuite() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(TWO, SPADES),
            PlayingCard(TWO, SPADES)
        )

        assertTrue(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoWildCards() {
        val meldValidator = MeldValidator()
        val meld = listOf(PlayingCard(JOKER, SPADES), PlayingCard(TWO, SPADES), PlayingCard(FIVE, SPADES))

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoIdenticalCardsInStraight() {
        val meldValidator = MeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADES),
            PlayingCard(FOUR, SPADES),
            PlayingCard(FOUR, SPADES),
            PlayingCard(FIVE, SPADES)
        )

        assertFalse(meldValidator.isValidMeld(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue() {
        val meldValidator = MeldValidator()

        val meld = listOf(PlayingCard(KING, HEARTS), PlayingCard(KING, SPADES), PlayingCard(KING, DIAMONDS))
        assertTrue(meldValidator.isValidMeld(meld))
    }

}
import PlayingCard.Suit.*
import PlayingCard.Value.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class FastMeldValidatorTest {

    @Test
    fun shouldBeAbleToMeldFullSuiteOfCards() {
        val meldValidator = FastMeldValidator()

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
            PlayingCard(ACE, SPADE) //only valid if jolly, not ace
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoTwos() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(TWO, SPADE),
            PlayingCard(TWO, HEART),
            PlayingCard(THREE, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoAndJoker() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(TWO, SPADE),
            PlayingCard(JOKER),
            PlayingCard(THREE, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoQueensAndAKing() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE)
        )

        assertFalse(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoAndTwoKings() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(TWO, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(THREE, SPADE)
        )

        assertFalse(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeWildcards() {
        val meldValidator = FastMeldValidator()

        assertFalse(
            meldValidator.isValid(
                listOf(
                    PlayingCard(TWO, SPADE),
                    PlayingCard(TWO, HEART),
                    PlayingCard(TWO, SPADE)
                )
            )
        )

        assertFalse(
            meldValidator.isValid(
                listOf(
                    PlayingCard(JOKER),
                    PlayingCard(JOKER),
                    PlayingCard(JOKER)
                )
            )
        )
    }

    @Test
    fun shouldBeAbleToMeldThreeStraightCards() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(JACK, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwo() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(TWO, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndAnAce() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(ACE, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTenThroughToKing() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(JACK, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(TEN, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwoInTheMiddle() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(FIVE, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndAJokerInTheMiddle() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(JOKER),
            PlayingCard(FIVE, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeNonConsecutiveCards() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SEVEN, SPADE)
        )

        assertFalse(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldThreeConsecutiveCardsDifferentSuites() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, CLUB),
            PlayingCard(FIVE, SPADE)
        )

        assertFalse(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoCards() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE)
        )

        assertFalse(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldFourStraightCards() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SIX, SPADE)
        )
        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldTwoTwosSameSuite() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(TWO, SPADE)
        )

        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldNotBeAbleToMeldTwoWildCards() {
        val meldValidator = FastMeldValidator()
        val meld = listOf(PlayingCard(JOKER, SPADE), PlayingCard(TWO, SPADE), PlayingCard(FIVE, SPADE))

        assertFalse(meldValidator.isValid(meld))
    }


    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(KING, HEART),
            PlayingCard(KING, SPADE),
            PlayingCard(KING, DIAMOND)
        )
        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue2() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(FIVE, HEART),
            PlayingCard(FIVE, SPADE),
            PlayingCard(FIVE, DIAMOND),
            PlayingCard(FIVE, DIAMOND),
            PlayingCard(FIVE, CLUB),
        )
        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue3() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(TWO, HEART),
            PlayingCard(TEN, CLUB),
            PlayingCard(TEN, SPADE),
        )
        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue4() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(JOKER),
            PlayingCard(TEN, CLUB),
            PlayingCard(TEN, SPADE),
        )
        assertTrue(meldValidator.isValid(meld))
    }

    @Test
    fun shouldBeAbleToMeldSequenceWithTwo() {
        val meldValidator = FastMeldValidator()

        val meld = listOf(
            PlayingCard(FOUR, CLUB),
            PlayingCard(SIX, CLUB),
            PlayingCard(SEVEN, CLUB),
            PlayingCard(EIGHT, CLUB),
            PlayingCard(TWO, SPADE),
        )

        // 4♣, 6♣, 7♣, 8♣, 2♠
        assertTrue(meldValidator.isValid(meld))
    }


}
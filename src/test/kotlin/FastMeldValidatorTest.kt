import PlayingCard.Suit.*
import PlayingCard.Value.*
import meld.Meld
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class FastMeldValidatorTest {

//    1. meld.Meld A♥,K♥,K♥,5♥
//    2. meld.Meld A♥,K♥,K♥
//    3. meld.Meld A♣,9♣,K♣,8♣,K♣,10♣
//    4. meld.Meld A♣,9♣,K♣,8♣,K♣

    @Test
    fun shouldNotBeAbleToMeldAceKingKing() {
        val meld = listOf(
            PlayingCard(ACE, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(KING, SPADE)
        )
        assertFalse(Meld(meld).valid)
    }

//    2♦,6♦,2♠,8♦,9♦

    @Test
    fun shouldNotBeAbleToMeldTwoWildcards() {
        val meld = Meld(listOf(
            PlayingCard(TWO, DIAMOND),
            PlayingCard(SIX, DIAMOND),
            PlayingCard(TWO, SPADE),
            PlayingCard(EIGHT, DIAMOND),
            PlayingCard(NINE, DIAMOND)
        ))
        assertFalse(meld.valid)
    }

    @Test
    fun shouldNotBeAbleToMeldFullSuiteWithTwoAces() {
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

        assertFalse(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoTwos() {
        val meld = Meld(listOf(
            PlayingCard(TWO, SPADE),
            PlayingCard(TWO, HEART),
            PlayingCard(THREE, SPADE)
        ))

        assertTrue(meld.valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoAndJoker() {
        val meld = listOf(
            PlayingCard(TWO, SPADE),
            PlayingCard(JOKER),
            PlayingCard(THREE, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldTwoQueensAndAKing() {


        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE)
        )

        assertFalse(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldTwoAndTwoKings() {


        val meld = listOf(
            PlayingCard(TWO, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(THREE, SPADE)
        )

        assertFalse(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldThreeWildcards() {
        assertFalse(
            Meld(
                listOf(
                    PlayingCard(TWO, SPADE),
                    PlayingCard(TWO, HEART),
                    PlayingCard(TWO, SPADE)
                )
            ).valid
        )

        assertFalse(
            Meld(
                listOf(
                    PlayingCard(JOKER),
                    PlayingCard(JOKER),
                    PlayingCard(JOKER)
                )
            ).valid
        )
    }

    @Test
    fun shouldBeAbleToMeldThreeStraightCards() {


        val meld = listOf(
            PlayingCard(JACK, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwo() {


        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(TWO, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndAnAce() {


        val meld = listOf(
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(ACE, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTenThroughToKing() {


        val meld = listOf(
            PlayingCard(JACK, SPADE),
            PlayingCard(QUEEN, SPADE),
            PlayingCard(KING, SPADE),
            PlayingCard(TEN, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndATwoInTheMiddle() {


        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(FIVE, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoStraightCardsAndAJokerInTheMiddle() {


        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(JOKER),
            PlayingCard(FIVE, SPADE)
        )

        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldThreeNonConsecutiveCards() {


        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SEVEN, SPADE)
        )

        assertFalse(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldThreeConsecutiveCardsDifferentSuites() {


        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, CLUB),
            PlayingCard(FIVE, SPADE)
        )

        assertFalse(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldTwoCards() {
        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE)
        )

        assertFalse(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldFourStraightCards() {

        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(FOUR, SPADE),
            PlayingCard(FIVE, SPADE),
            PlayingCard(SIX, SPADE)
        )
        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldTwoTwosSameSuite() {
        val meld = listOf(
            PlayingCard(THREE, SPADE),
            PlayingCard(TWO, SPADE),
            PlayingCard(TWO, SPADE)
        )
        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldNotBeAbleToMeldTwoWildCards() {
        val meld = Meld(listOf(
            PlayingCard(JOKER),
            PlayingCard(TWO, SPADE),
            PlayingCard(FIVE, SPADE)
        ))
        assertFalse(meld.valid)
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue() {
        val meld = Meld(
            listOf(
                PlayingCard(KING, HEART),
                PlayingCard(KING, SPADE),
                PlayingCard(KING, DIAMOND)
            )
        )
        assertTrue(meld.valid)
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue2() {


        val meld = listOf(
            PlayingCard(FIVE, HEART),
            PlayingCard(FIVE, SPADE),
            PlayingCard(FIVE, DIAMOND),
            PlayingCard(FIVE, DIAMOND),
            PlayingCard(FIVE, CLUB),
        )
        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue3() {


        val meld = listOf(
            PlayingCard(TWO, HEART),
            PlayingCard(TEN, CLUB),
            PlayingCard(TEN, SPADE),
        )
        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldThreeCardsOfSameValue4() {


        val meld = listOf(
            PlayingCard(JOKER),
            PlayingCard(TEN, CLUB),
            PlayingCard(TEN, SPADE),
        )
        assertTrue(Meld(meld).valid)
    }

    @Test
    fun shouldBeAbleToMeldSequenceWithTwo() {


        val meld = listOf(
            PlayingCard(FOUR, CLUB),
            PlayingCard(SIX, CLUB),
            PlayingCard(SEVEN, CLUB),
            PlayingCard(EIGHT, CLUB),
            PlayingCard(TWO, SPADE),
        )

        // 4♣, 6♣, 7♣, 8♣, 2♠
        assertTrue(Meld(meld).valid)
    }


}
import PlayingCard.Suit.DIAMOND
import PlayingCard.Suit.HEART
import PlayingCard.Value
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.test.assertTrue
import org.hamcrest.CoreMatchers.`is` as coreMatchersIs

internal class HandEvaluatorTest {

    @Test
    fun getMeldsReturnsValidSequence() {
        val evaluator = HandEvaluator()
        val cards = listOf(
            PlayingCard(Value.JACK, DIAMOND),
            PlayingCard(Value.QUEEN, DIAMOND),
            PlayingCard(Value.KING, DIAMOND)
        )

        val validMelds = evaluator.getMelds(cards)
        assertTrue(validMelds.size == 1)

        assertThat(validMelds[0], coreMatchersIs(cards))
    }

    @Test
    fun getMeldsReturnsValidSequenceWithWildCard() {
        val evaluator = HandEvaluator()
        val cards = listOf(
            PlayingCard(Value.JACK, DIAMOND),
            PlayingCard(Value.KING, DIAMOND),
            PlayingCard(Value.TWO, DIAMOND)
        )

        val validMelds = evaluator.getMelds(cards)
        assertTrue(validMelds.size == 1)

        assertThat(validMelds[0], coreMatchersIs(cards))
    }

    @Test
    fun getMeldsReturnsValidSubSequences() {
        val evaluator = HandEvaluator()
        val cards = listOf(
            PlayingCard(Value.SIX, DIAMOND),
            PlayingCard(Value.SEVEN, DIAMOND),
            PlayingCard(Value.EIGHT, DIAMOND),
            PlayingCard(Value.JACK, DIAMOND),
            PlayingCard(Value.QUEEN, DIAMOND),
            PlayingCard(Value.KING, DIAMOND)
        )

        val validMelds = evaluator.getMelds(cards)

        assertThat(
            validMelds[0], coreMatchersIs(
                listOf(
                    PlayingCard(Value.SIX, DIAMOND),
                    PlayingCard(Value.SEVEN, DIAMOND),
                    PlayingCard(Value.EIGHT, DIAMOND)
                )
            )
        )

        assertThat(
            validMelds[1], coreMatchersIs(
                listOf(
                    PlayingCard(Value.JACK, DIAMOND),
                    PlayingCard(Value.QUEEN, DIAMOND),
                    PlayingCard(Value.KING, DIAMOND)
                )
            )
        )
    }

    @Test
    fun getMeldsReturnsValidCombination() {
        val evaluator = HandEvaluator()
        val cards = listOf(
            PlayingCard(Value.JACK, DIAMOND),
            PlayingCard(Value.JACK, DIAMOND),
            PlayingCard(Value.JACK, HEART),
            PlayingCard(Value.ACE, HEART)
        )

        val validMelds = evaluator.getMelds(cards)
        assertTrue(validMelds.size == 1)

        assertThat(
            validMelds[0], coreMatchersIs(
                listOf(
                    PlayingCard(Value.JACK, HEART),
                    PlayingCard(Value.JACK, DIAMOND),
                    PlayingCard(Value.JACK, DIAMOND)
                )
            )
        )
    }

}
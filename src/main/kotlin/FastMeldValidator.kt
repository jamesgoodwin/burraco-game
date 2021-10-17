import PlayingCard.Suit.*
import PlayingCard.Value.JOKER
import PlayingCard.Value.TWO

class FastMeldValidator : MeldValidator {

    private val cardValues: List<Float> = PlayingCard.Value.values().map { it.binaryValue.toFloat() }
    private val suitValues: List<Float> = PlayingCard.Suit.values().map { it.binaryValue.toFloat() }

    // 1. evaluate if a meld is valid or not, sequence or combination
    override fun isValid(cards: List<PlayingCard>): Boolean {
        if (cards.size !in 3..14) {
            return false
        }

        val sumSuits: Int = cards.sumBy { if (it.suit != null) it.suit.binaryValue else 0 }

        val avgSuit = sumSuits.toFloat() / cards.size.toFloat()
        val avgSuitMinusSpade = (sumSuits.toFloat() - SPADE.binaryValue) / (cards.size.toFloat() - 1)
        val avgSuitMinusHeart = (sumSuits.toFloat() - HEART.binaryValue) / (cards.size.toFloat() - 1)
        val avgSuitMinusDiamond = (sumSuits.toFloat() - DIAMOND.binaryValue) / (cards.size.toFloat() - 1)
        val avgSuitMinusClub = (sumSuits.toFloat() - CLUB.binaryValue) / (cards.size.toFloat() - 1)

        val sumValues: Int = cards.sumBy { it.value.binaryValue }
        val avgValue = sumValues.toFloat() / cards.size.toFloat()

        if (suitValues.any { it == avgSuit || it == avgSuitMinusSpade || it == avgSuitMinusHeart || it == avgSuitMinusDiamond || it == avgSuitMinusClub }
            && avgValue !in cardValues) {
            if (validSequence(sumValues, avgSuit, cards, suitValues, avgSuitMinusSpade, avgSuitMinusHeart, avgSuitMinusDiamond, avgSuitMinusClub)) return true
        } else {
            if (validCombination(sumValues, cards, avgValue)) return true
        }

        return false
    }

    private fun validCombination(sumValues: Int, playingCards: List<PlayingCard>, avgValue: Float): Boolean {
        val avgValueMinusTwo = (sumValues.toFloat() - TWO.binaryValue) / (playingCards.size.toFloat() - 1)
        val avgValueMinusTwoTwos =
            ((sumValues.toFloat() - TWO.binaryValue) - TWO.binaryValue) / (playingCards.size.toFloat() - 2)
        val avgValueMinusJoker = (sumValues.toFloat() - JOKER.binaryValue) / (playingCards.size.toFloat() - 1)

        if (cardValues.minus(TWO.binaryValue.toFloat()).minus(JOKER.binaryValue.toFloat())
                .any { it == avgValue || it == avgValueMinusTwo || it == avgValueMinusTwoTwos || it == avgValueMinusJoker }
        ) {
            return true
        }

        return false
    }

    private fun validSequence(
        sumValues: Int,
        avgSuit: Float,
        cards: List<PlayingCard>,
        suitValues: List<Float>,
        avgSuitMinusSpade: Float,
        avgSuitMinusHeart: Float,
        avgSuitMinusDiamond: Float,
        avgSuitMinusClub: Float
    ): Boolean {
        // validate sequence
        val lsb = sumValues.takeLowestOneBit()
        val result = sumValues / lsb

        // check if all 1s therefore valid sequence
        if (Integer.toBinaryString(result).length == cards.size && avgSuit in suitValues) {
            return true
        }

        // check if we have a 2 to play with !if 2 twos this will not work!!
        if (lsb == 1
            || (avgSuitMinusSpade in suitValues && (cards.contains(PlayingCard(TWO, SPADE))))
            || (avgSuitMinusHeart in suitValues && (cards.contains(PlayingCard(TWO, HEART))))
            || (avgSuitMinusDiamond in suitValues && (cards.contains(PlayingCard(TWO, DIAMOND))))
            || (avgSuitMinusClub in suitValues && (cards.contains(PlayingCard(TWO, CLUB))))
        ) {
            val newBinaryValues = sumValues.shr(1)
            val newLsb = newBinaryValues.takeLowestOneBit()
            if (binaryZeros(newBinaryValues / newLsb) <= 1) {
                return true
            }
        }
        // highest one bit could be there if we had two kings as well?
        if (sumValues.takeHighestOneBit() == JOKER.binaryValue && binaryZeros((sumValues - JOKER.binaryValue) / sumValues.takeLowestOneBit()) <= 1) {
            return true
        }
        return false
    }

    // 2. todo - evaluate if burraco, semi-clean burraco, dirty burraco, or none

    private fun binaryZeros(n: Int): Int {
        var number = n
        var zeroCount = 0
        // Run a while loop until n is greater than or equals to 1
        while (number >= 1) {
            /* Use modulo operator to get the reminder of division by 2 (reminder will be 1 or 0 as you are dividing by 2).
           Keep in mind that binary representation is an array of these reminders until the number is equal to 1.
           And once the number is equal to 1 the reminder is 1, so you can exit the loop there.*/
            if (number % 2 == 0) {
                zeroCount++
            }
            number /= 2
        }
        return zeroCount
    }

}
import PlayingCard.Value.JOKER
import PlayingCard.Value.TWO

class FastMeldValidator {

    private val cardValues: List<Float> = PlayingCard.Value.values().map { it.binaryValue.toFloat() }
    private val suitValues: List<Float> = PlayingCard.Suit.values().map { it.binaryValue.toFloat() }

    // 1. evaluate if a meld is valid or not, sequence or combination
    fun isValid(playingCards: List<PlayingCard>): Boolean {
        if (playingCards.size < 3 || playingCards.size > 14) {
            return false
        }

        val sumValues: Int = playingCards.sumBy { it.value.binaryValue }
        val sumSuits: Int = playingCards.sumBy { if (it.suit != null) it.suit.binaryValue else 0 }

        val avgSuit = sumSuits.toFloat() / playingCards.size.toFloat()
        val avgSuitMinusTwo = (sumSuits.toFloat() - TWO.binaryValue) / (playingCards.size.toFloat() - 1)
        val avgSuitMinusJoker = sumSuits.toFloat() / (playingCards.size.toFloat() - 1)
        val avgValue = sumValues.toFloat() / playingCards.size.toFloat()

        if ((avgSuit in suitValues
                    || avgSuitMinusTwo in suitValues
                    || avgSuitMinusJoker in suitValues) && avgValue !in cardValues
        ) {
            // validate sequence
            val lsb = sumValues.takeLowestOneBit()
            val result = sumValues / lsb

            // check if all 1s therefore valid sequence
            if (((result + 1) and result == 0) and (result != 0)) {
                return true
            }

            // check if we have a 2 to play with
            // ! if 2 twos this will not work!!
            if (lsb == 1) {
                if (avgSuitMinusTwo in suitValues) {
                    return true
                }

                val newBinaryValues = sumValues.shr(1)
                val newLsb = newBinaryValues.takeLowestOneBit()
                if (binaryZeros(newBinaryValues / newLsb) <= 1) {
                    return true
                }
            }
            if (binaryZeros((sumValues - JOKER.binaryValue) / sumValues.takeLowestOneBit()) <= 1) {
                return true
            }
        } else {
            for (value in PlayingCard.Value.values()) {
                // check for clean meld
                if (avgValue == value.binaryValue.toFloat()) {
                    return when (value) {
                        TWO, JOKER -> false
                        else -> true
                    }
                }
            }
        }

        return false
    }

    // 2. evaluate if burraco, semi-clean burraco, dirty burraco, or none

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
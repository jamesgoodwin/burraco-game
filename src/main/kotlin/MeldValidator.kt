import PlayingCard.*

class MeldValidator {

    fun isValidMeld(meld: List<PlayingCard>): Boolean {
        if (meld.size < 3 || meld.size > 14) {
            return false
        }

        val wildcards = mutableListOf<PlayingCard>()
        val aces = mutableListOf<PlayingCard>()
        val regularCards = mutableListOf<PlayingCard>()

        var sameSuite = true
        var sameValue = true
        var suite: Suite? = null
        var value: Value? = null

        for (card in meld) {
            when (card.value) {
                Value.ACE -> aces.add(card)
                Value.TWO -> wildcards.add(card)
                Value.JOKER -> wildcards.add(card)
                else -> {
                    regularCards.add(card)
                    if (suite == null) suite = card.suite
                    if (value == null) value = card.value

                    if (suite != card.suite) {
                        sameSuite = false
                    }
                    if (value != card.value) {
                        sameValue = false
                    }
                    if (!sameSuite && !sameValue) {
                        break
                    }
                }
            }
        }

        return if (sameSuite) {
            validateSameSuite(regularCards, aces, wildcards, suite, meld)
        } else {
            validateSameValue(regularCards, aces, wildcards, suite, meld)
        }
    }

    private fun validateSameValue(
        regularCards: MutableList<PlayingCard>,
        aces: MutableList<PlayingCard>,
        wildcards: MutableList<PlayingCard>,
        suite: Suite?,
        meld: List<PlayingCard>
    ): Boolean {
        return regularCards.zipWithNext { a, b -> a.value == b.value }.all { it }
    }

    private fun validateSameSuite(
        regularCards: MutableList<PlayingCard>,
        aces: MutableList<PlayingCard>,
        wildcards: MutableList<PlayingCard>,
        suite: Suite?,
        meld: List<PlayingCard>
    ): Boolean {
        val suiteArray = arrayOfNulls<PlayingCard>(14)
        var minIndex = -1
        var maxIndex = -1

        for (card: PlayingCard in regularCards) {
            if (!suiteArray.contains(card)) {
                suiteArray[card.value.ordinal - 1] = card

                if (minIndex == -1 || card.value.ordinal < minIndex) {
                    minIndex = card.value.ordinal - 1
                }
                if (maxIndex == -1 || card.value.ordinal > maxIndex) {
                    maxIndex = card.value.ordinal - 1
                }
            }
        }

        if (aces.isNotEmpty()) {
            if (aces.size == 2) {
                suiteArray[0] = aces[0]
                minIndex = 0
                suiteArray[13] = aces[1]
                maxIndex = 13
            } else {
                // is min or max nearer to 0 or 13?
                if ((13 - maxIndex) < minIndex) {
                    suiteArray[13] = aces[0]
                    maxIndex = 13
                } else {
                    suiteArray[0] = aces[0]
                    minIndex = 0
                }
            }
        }

        if (wildcards.isNotEmpty()) {
            val numGaps = ((maxIndex - minIndex) + 1) - (aces.size + regularCards.size)
            if (numGaps > wildcards.size) {
                return false
            }

            if (wildcards.size == 2) { // 2 must be used as a 2 instead of a wildcard
                if (wildcards.contains(PlayingCard(Value.TWO, suite))) {
                    suiteArray[1] = PlayingCard(Value.TWO, suite)
                    wildcards.remove(PlayingCard(Value.TWO, suite))
                    if (1 < minIndex) {
                        minIndex = 1
                    }
                }
            }

            // now always 1 wildcard left, fill it in
            when {
                (maxIndex - minIndex) == (meld.size - 1) -> { // in the middle
                    for (i in minIndex..maxIndex) {
                        if (suiteArray[i] == null) {
                            suiteArray[i] = wildcards[0]
                        }
                    }
                }
                suiteArray[minIndex - 1] == null -> { // at the beginning
                    suiteArray[minIndex - 1] = wildcards[0]
                    minIndex--
                }
                suiteArray[maxIndex + 1] == null -> { // at the end
                    suiteArray[maxIndex + 1] = wildcards[0]
                    maxIndex++
                }
            }

            // fill the gaps
            for (i in minIndex..maxIndex) {
                if (suiteArray[i] == null && wildcards.isNotEmpty()) {
                    suiteArray[i] = wildcards.removeAt(0)
                }
            }
        }

        return (maxIndex - minIndex) == (meld.size - 1)
    }

    /** check if valid hand
     *
     * 1. Remove any Aces, since they can go at the start or end.
     * 2. If there are two 2s, remove only 1
     * 3. If there is one 2, remove it
     * 3. Remove any Jokers
     * 4. Place the remaining numbers into an array
     *    [ ] [ ] [X] [X] [2] [J] [X]
     *     0   1   2   3   4   5   6
     *
     * 5. Check if there are gaps in the range
     * 6. If num. gaps <= num wildcards, fill in the gaps
     * 7. If Ace left over place it at the beginning or the end, or if 1 space gap and a wildcard, place the wildcard
     * 8. If wildcard left over, place it at beginning or the end
     *
     * -- Max possible positions is 14 [A,2,3,4,5,6,7,8,9,10,J,Q,K,A]
     */


}
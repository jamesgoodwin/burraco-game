import PlayingCard.Value.*

data class Meld(val cards: List<PlayingCard>) {

    var value: PlayingCard.Value?
    var suit: PlayingCard.Suit?
    private val wildcard: (PlayingCard) -> Boolean = { it.value == TWO || it.value == JOKER }

    private val meldCardValues = cards.map { it.value }

    val valid: Boolean
    private val cardsOrdered: List<PlayingCard>

    private val regularCards = cards.filterNot(wildcard).sorted()
    private val wildcards = cards.filter(wildcard)

    init {
        var (meldSuit, meldValue: PlayingCard.Value?) = checkSuitAndValue()

        // cannot be a combination in scenario of two wildcards so null it, i.e. Two and a Joker
        meldValue = if (wildcards.size > 1) null else meldValue

        when {
            meldValue != null -> {
                // check combination
                val (list, validSequence) = checkCombination()
                cardsOrdered = list
                valid = validSequence
            }
            meldSuit != null -> {
                // check sequence
                val (list, validSequence) = checkSequence()
                cardsOrdered = list
                valid = validSequence
            }
            else -> {
                cardsOrdered = cards
                valid = false
            }
        }

        suit = meldSuit
        value = meldValue
    }

    private fun checkSuitAndValue(): Pair<PlayingCard.Suit?, PlayingCard.Value?> {
        val cardsLeft = regularCards.toMutableList()
        val first = cardsLeft.removeFirstOrNull()
        var suit = first?.suit
        var value: PlayingCard.Value? = first?.value

        cardsLeft.forEach {
            if (it.suit != suit) {
                suit = null
            }
            if (it.value != value) {
                value = null
            }
        }
        return Pair(suit, value)
    }

    private fun checkCombination(): Pair<List<PlayingCard>, Boolean> {
        val valid = regularCards.size >= 3 && wildcards.size <= 1 || regularCards.size == 2 && wildcards.size == 1
        return Pair(cards, valid)
    }

    private fun checkSequence(): Pair<List<PlayingCard>, Boolean> {
        val cardsLeft = (regularCards + wildcards).toMutableList()

        if (cardsLeft.size !in 3..14) return Pair(cardsLeft, false)

        var previousCard: PlayingCard = cardsLeft.removeFirst()
        var sequential = true
        var wildCardUsed = false
        val cardsBeingOrdered = mutableListOf(previousCard)

        while (cardsLeft.isNotEmpty() && sequential) {
            val nextCard = cardsLeft.removeFirst()

            if (nextCard.value.ordinal == previousCard.value.ordinal + 1) {
                previousCard = nextCard
                cardsBeingOrdered.add(nextCard)
            } else if (nextCard.value.ordinal == previousCard.value.ordinal + 2
                && !wildCardUsed && (meldCardValues.contains(JOKER) || meldCardValues.contains(TWO)) // todo - needs to be checked using cards left instead of all cards
            ) {
                wildCardUsed = true
                previousCard = nextCard
                cardsBeingOrdered.add(nextCard)
                // todo - need to add the wildcard used
                cardsLeft.removeIf(wildcard)
            } else if (previousCard.value == THREE && nextCard.value == TWO) {
                // two becomes a natural two, swap previous and next
                cardsBeingOrdered.add(cardsBeingOrdered.indexOf(previousCard), nextCard)
            } else if (wildcard(nextCard) && !wildCardUsed) {
                wildCardUsed = true
                if (cardsLeft.isEmpty()) {
                    cardsBeingOrdered.add(0, nextCard)
                } else {
                    cardsBeingOrdered.add(nextCard)
                }
            } else sequential = false
        }

        return Pair(cardsBeingOrdered.toList(), sequential)
    }
}
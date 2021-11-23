import PlayingCard.Value.*

data class Meld(val cards: List<PlayingCard>) {

    var value: PlayingCard.Value?
    var suit: PlayingCard.Suit?
    private val wildcard: (PlayingCard) -> Boolean = { it.value == TWO || it.value == JOKER }

    val valid: Boolean
    val cardsOrdered: List<PlayingCard>

    private val regularCards = cards.filterNot(wildcard).sorted()
    private val wildcards = cards.filter(wildcard)

    init {
        var (meldSuit, meldValue: PlayingCard.Value?) = checkSuitAndValue()
        // cannot be a combination in scenario of two wildcards so null it, i.e. Two and a Joker
        meldValue = if (wildcards.size > 1) null else meldValue
        suit = meldSuit
        value = meldValue

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
    }

    private fun checkSequence(): Pair<List<PlayingCard>, Boolean> {
        val aceToBeUsed = regularCards.singleOrNull { it.value == ACE }
        val wildCardsToBeUsed = wildcards.toMutableList()

        val cardsBeingOrdered: MutableList<PlayingCard> = regularCards.toMutableList()
        cardsBeingOrdered.remove(aceToBeUsed)
        cardsBeingOrdered.sort()

        val gapIndex = gapIndex(cardsBeingOrdered)
        val naturalTwo = PlayingCard(TWO, suit)

        if (gapIndex > 0 && wildCardsToBeUsed.isNotEmpty()) {
            val wildcard = getWildcardNormalPreferred(wildCardsToBeUsed)
            if (wildcard != null) {
                cardsBeingOrdered.add(gapIndex, wildcard)
                wildCardsToBeUsed.remove(wildcard)
            }
        }

        addAces(aceToBeUsed, cardsBeingOrdered, wildCardsToBeUsed)

        if (wildCardsToBeUsed.isNotEmpty()) {
            if (wildCardsToBeUsed.contains(naturalTwo) && cardsBeingOrdered.first() == PlayingCard(THREE, suit)) {
                cardsBeingOrdered.add(0, naturalTwo)
                wildCardsToBeUsed.remove(naturalTwo)
            }
        }

        if (cardsBeingOrdered.size !in 3..14) return Pair(cardsBeingOrdered, false)

        return Pair(cardsBeingOrdered.toList(), gapIndex(cardsBeingOrdered) == -1)
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

    private fun addAces(
        aceToBeUsed: PlayingCard?,
        cardsBeingOrdered: MutableList<PlayingCard>,
        wildCardsToBeUsed: MutableList<PlayingCard>
    ) {
        if (aceToBeUsed != null) {
            if (cardsBeingOrdered.contains(PlayingCard(KING, suit))) {
                cardsBeingOrdered.add(aceToBeUsed)
            } else {
                if (cardsBeingOrdered.first() == PlayingCard(THREE, suit) && wildCardsToBeUsed.isNotEmpty()) {
                    getWildcardNaturalPreferred(wildCardsToBeUsed)?.let {
                        cardsBeingOrdered.add(0, aceToBeUsed)
                        cardsBeingOrdered.add(1, it)
                    }
                } else if (cardsBeingOrdered.last() == PlayingCard(QUEEN, suit) && wildCardsToBeUsed.isNotEmpty()) {
                    val wildcard = getWildcardNormalPreferred(wildCardsToBeUsed)
                    if (wildcard != null) {
                        cardsBeingOrdered.add(wildcard)
                    }
                    cardsBeingOrdered.add(PlayingCard(QUEEN, suit))
                }
            }
        }
    }

    private fun getWildcardNormalPreferred(wildcards: MutableList<PlayingCard>): PlayingCard? {
        val naturalTwo = PlayingCard(TWO, suit)
        val normalWildcard = wildcards.singleOrNull { it != naturalTwo }

        if (normalWildcard != null) {
            return normalWildcard
        }
        if (wildcards.contains(naturalTwo)) {
            return naturalTwo
        }
        return null
    }

    private fun getWildcardNaturalPreferred(wildcards: MutableList<PlayingCard>): PlayingCard? {
        val naturalTwo = PlayingCard(TWO, suit)
        if (wildcards.contains(naturalTwo)) {
            return naturalTwo
        }

        val normalWildcard = wildcards.singleOrNull { it != naturalTwo }
        if (normalWildcard != null) {
            return normalWildcard
        }
        return null
    }

    private fun gapIndex(cardsBeingOrdered: MutableList<PlayingCard>): Int {
        var previous = cardsBeingOrdered.first()
        cardsBeingOrdered.minus(previous).forEach {
            if (it.value.ordinal != previous.value.ordinal + 1) {
                if (it.value == JOKER || it.value == TWO) {
                    val nextValue = PlayingCard.Value.from(previous.value.order + 1)
                    if (nextValue != null) {
                        previous = PlayingCard(nextValue, previous.suit)
                    }
                } else return cardsBeingOrdered.indexOf(it)
            } else previous = it
        }
        return -1
    }

    override fun toString(): String {
        return cardsOrdered.joinToString(separator = ",")
    }

}
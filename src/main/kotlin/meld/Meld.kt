package meld

import PlayingCard
import PlayingCard.Value.*

data class Meld(val cards: List<PlayingCard>) {

    var value: PlayingCard.Value?
    var suit: PlayingCard.Suit?
    private val wildcard: (PlayingCard) -> Boolean = { it.value == TWO || it.value == JOKER }

    val valid: Boolean
    val cardsOrdered: List<PlayingCard>
    var wildcardIndex = -1

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
        val wildCardsToBeUsed: MutableList<PlayingCard> = wildcards.toMutableList()

        val cardsBeingOrdered: MutableList<PlayingCard> = regularCards.toMutableList()
        cardsBeingOrdered.remove(aceToBeUsed)
        cardsBeingOrdered.sort()

        val gapIndex = if (cardsBeingOrdered.isNotEmpty()) gapIndex(cardsBeingOrdered) else -1
        var wildCardUsed = false
        var naturalTwoUsed: Boolean

        // 3 .. King
        if (gapIndex > 0 && wildCardsToBeUsed.isNotEmpty()) {
            val wildcard = getWildcardNormalPreferred(wildCardsToBeUsed)
            if (wildcard != null) {
                wildcardIndex = gapIndex
                cardsBeingOrdered.add(gapIndex, wildcard)
                wildCardsToBeUsed.remove(wildcard)
                wildCardUsed = true
            }
        }

        // fill in aces and wildcard if required
        naturalTwoUsed = addAces(aceToBeUsed, cardsBeingOrdered, wildCardsToBeUsed)

        val wildcardToBeUsedAsNaturalTwo = cardsBeingOrdered.first().value == THREE
        if (wildCardsToBeUsed.isNotEmpty() && (!wildCardUsed || (wildcardToBeUsedAsNaturalTwo && !naturalTwoUsed))) {
            val wildcard: Pair<Boolean, PlayingCard>? = getWildcardNaturalPreferred(wildCardsToBeUsed)
            if (wildcard != null) {
                if (!wildcardToBeUsedAsNaturalTwo) {
                    wildcardIndex = 0
                }
                cardsBeingOrdered.add(0, wildcard.second)
                wildCardsToBeUsed.remove(wildcard.second)
                if (wildcard.first && wildcardToBeUsedAsNaturalTwo) naturalTwoUsed = true else wildCardUsed = true
            }
        }

        // check if wildcard used and not a natural two
        if (wildCardsToBeUsed.isNotEmpty() && (!wildCardUsed || (wildcardToBeUsedAsNaturalTwo && !naturalTwoUsed))) {
            val wildcard = getWildcardNaturalPreferred(wildCardsToBeUsed)
            if (wildcard != null) {
                cardsBeingOrdered.add(0, wildcard.second)
                wildCardsToBeUsed.remove(wildcard.second)
            }
        }

        if (cardsBeingOrdered.size !in 3..14) return Pair(cardsBeingOrdered, false)

        return Pair(cardsBeingOrdered.toList(), gapIndex(cardsBeingOrdered) == -1 && wildCardsToBeUsed.isEmpty())
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
        wildCardsToBeUsed: MutableList<PlayingCard>,
    ): Boolean {
        var naturalTwoWildcardUsed = false
        if (aceToBeUsed != null) {
            if (cardsBeingOrdered.contains(PlayingCard(KING, suit))) {
                cardsBeingOrdered.add(aceToBeUsed)
            } else {
                if (cardsBeingOrdered.firstOrNull() == PlayingCard(THREE, suit) && wildCardsToBeUsed.isNotEmpty()) {
                    val wildcard = getWildcardNaturalPreferred(wildCardsToBeUsed)
                    if (wildcard != null) {
                        cardsBeingOrdered.add(0, aceToBeUsed)
                        cardsBeingOrdered.add(1, wildcard.second)
                        wildcardIndex = 1
                        wildCardsToBeUsed.remove(wildcard.second)
                        naturalTwoWildcardUsed = true
                    }
                } else if (cardsBeingOrdered.lastOrNull() == PlayingCard(QUEEN,
                        suit) && wildCardsToBeUsed.isNotEmpty()
                ) {
                    val wildcard = getWildcardNormalPreferred(wildCardsToBeUsed)
                    if (wildcard != null) {
                        cardsBeingOrdered.add(wildcard)
                        wildCardsToBeUsed.remove(wildcard)
                        wildcardIndex = cardsBeingOrdered.indexOf(wildcard)
                    }
                    cardsBeingOrdered.add(PlayingCard(QUEEN, suit))
                } else {
                    cardsBeingOrdered.add(aceToBeUsed)
                }
            }
        }
        return naturalTwoWildcardUsed
    }

    fun getBurracoType(): Burraco {
        if (wildcardIndex == -1 && cardsOrdered.size >= 7) return Burraco.PULITO

        val cardsAboveOrBelowWildcard =
            if (wildcardIndex > cardsOrdered.size / 2) wildcardIndex
            else cardsOrdered.size - (wildcardIndex + 1)

        return when {
            cardsAboveOrBelowWildcard >= 7 -> Burraco.SEMI_PULITO
            cardsOrdered.size >= 7 -> Burraco.SPORCO
            else -> Burraco.NO_BURRACO
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

    private fun getWildcardNaturalPreferred(wildcards: MutableList<PlayingCard>): Pair<Boolean, PlayingCard>? {
        val naturalTwo = PlayingCard(TWO, suit)
        if (wildcards.contains(naturalTwo)) {
            return Pair(true, naturalTwo)
        }

        val normalWildcard = wildcards.singleOrNull { it != naturalTwo }
        if (normalWildcard != null) {
            return Pair(false, normalWildcard)
        }
        return null
    }

    private fun gapIndex(cardsBeingOrdered: MutableList<PlayingCard>): Int {
        var previous = cardsBeingOrdered.first()
        cardsBeingOrdered.minus(previous).forEach {
            if (it.value.ordinal != previous.value.ordinal + 1) {
                if (it.value == JOKER || it.value == TWO) {
                    // check if natural two or wildcard to determine correct value in the sequence
                    val nextValue = if (previous.value == TWO) {
                        TWO
                    } else PlayingCard.Value.from(previous.value.order + 1)
                    if (nextValue != null) {
                        previous = PlayingCard(nextValue, previous.suit)
                    }
                } else if (previous.value == JOKER || previous.value == TWO) {
                    previous = it
                } else return cardsBeingOrdered.indexOf(it)
            } else previous = it
        }
        return -1
    }

    override fun toString(): String {
        return cardsOrdered.joinToString(separator = ",")
    }

}
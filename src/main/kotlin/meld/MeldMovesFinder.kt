package meld

import MovesFinder
import PlayingCard
import State
import memoize
import kotlin.math.max
import kotlin.math.min

class MeldMovesFinder : MovesFinder {

    val getNewSequenceMeldMovesCached = ::getNewSequenceMeldMovesSlow.memoize(5000)
    val getNewCombinationMeldMovesCached = ::getNewCombinationMeldMovesSlow.memoize(5000)

    override fun getAllMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): List<MeldMove> {
        val handBySuit = hand.groupBy { it.suit }
        val handByValue = hand.groupBy { it.value }
        val wildcards = hand.filter { it.wildcard }
        val allMoves = mutableListOf<MeldMove>()

        allMoves.let {
            it.addAll(getNewSequenceMeldMoves(handBySuit, wildcards))
            it.addAll(getMeldToExistingSequenceMoves(handBySuit, wildcards, melds))
            it.addAll(getNewCombinationMeldMoves(handByValue, wildcards))
            it.addAll(getMeldToExistingCombinationMoves(handByValue, wildcards, melds))
        }

        return allMoves
    }

    private fun getNewCombinationMeldMovesSlow(cards: List<PlayingCard>, wildcards: List<PlayingCard>): List<MeldMove> {
        val allCombinations: MutableList<List<PlayingCard>> = mutableListOf()
        if (cards.size >= 3) {
            allCombinations.add(cards)
        }
        if (cards.size >= 2 && wildcards.isNotEmpty()) {
            wildcards.forEach { wildcard ->
                val cardsUsed = cards.toMutableList()
                cardsUsed.add(wildcard)
                allCombinations.add(cardsUsed)
            }
        }

        return allCombinations
            .map {
                MeldAttempt(-1, it)
            }
            .filter { Meld(it.handCardsUsed).valid }
            .map {
                NewMeldMove(Meld(it.handCardsUsed))
            }.toList()
    }

    fun getNewCombinationMeldMoves(handByValue: Map<PlayingCard.Value, List<PlayingCard>>, wildcards: List<PlayingCard>, ): List<MeldMove> {
        return handByValue.map { handCards -> getNewCombinationMeldMovesCached(handCards.value, wildcards) }.flatten()
    }

    private fun getMeldToExistingCombinationMoves(
        handByValue: Map<PlayingCard.Value, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        melds: List<Meld>,
    ): List<MeldMove> {
        val meldsByValue = melds.groupBy { it.value }

        return meldsByValue.map { meldCards ->
            val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()

            meldCards.value.forEach { meld ->
                val index = melds.indexOf(meld)

                val handMatchingMeld = handByValue[meldCards.key]
                if (handMatchingMeld != null) {
                    allCombinations.add(Triple(index, handMatchingMeld, meld.cards))
                }

                if (wildcards.isNotEmpty()) {
                    wildcards.forEach { wildcard ->
                        allCombinations.add(Triple(index, listOf(wildcard), meld.cards))
                    }
                }
            }
            allCombinations
        }.flatten()
            .map {
                MeldAttempt(it.first, it.second, it.third)
            }
            .filter { Meld(it.handCardsUsed + it.existingMeld).valid }
            .map {
                ExistingMeldMove(it)
            }.toList()
            .distinctBy { }
    }

    fun getNewSequenceMeldMoves(
        handBySuit: Map<PlayingCard.Suit?, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
    ): List<MeldMove> {
        return handBySuit.map { handCards -> getNewSequenceMeldMovesCached(handCards.value, wildcards) }.flatten()
    }

    private fun getNewSequenceMeldMovesSlow(
        cards: List<PlayingCard>,
        wildcards: List<PlayingCard>,
    ): List<NewMeldMove> {
        val allCombinations: MutableList<List<PlayingCard>> = mutableListOf()
        // check for new melds
        if (cards.size >= 2) {
            allCombinations.add(cards)
        }

        return allCombinations
            .map { createWindows(it, wildcards) }
            .flatten()
            .filter { Meld(it.handCardsUsed).valid }
            .map {
                NewMeldMove(Meld(it.handCardsUsed))
            }.toList()
    }

    fun getMeldToExistingSequenceMoves(
        handBySuit: Map<PlayingCard.Suit?, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
        melds: List<Meld>,
    ): List<MeldMove> {
        val meldsBySuit = melds.groupBy { it.suit }

        return meldsBySuit.map { meldCards ->
            val allCombinations = mutableListOf<Triple<Int, List<PlayingCard>, List<PlayingCard>>>()

            meldCards.value.forEach { meld ->
                val index = melds.indexOf(meld)

                val handMatchingMeld = handBySuit[meldCards.key]
                if (handMatchingMeld != null) {
                    allCombinations.add(Triple(index, handMatchingMeld, meld.cards))
                }

                if (wildcards.isNotEmpty()) {
                    wildcards.forEach { wildcard ->
                        allCombinations.add(Triple(index, listOf(wildcard), meld.cards))
                    }
                }
            }
            allCombinations
        }.flatten()
            .map {
                createWindows(it, wildcards)
            }
            .flatten()
            .filter { Meld(it.meldCombo).valid }
            .map {
                ExistingMeldMove(it)
            }.toList()
            .distinctBy { it.toString() }
    }

    private fun createWindows(
        values: Triple<Int, List<PlayingCard>, List<PlayingCard>>,
        wildcards: List<PlayingCard>,
    ): List<MeldAttempt> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))

        if (values.third.isNotEmpty()) { //existing
            windows.addAll(getWindowsWithExistingMeld(values))
        } else if (values.second.isNotEmpty()) { //new
            windows.addAll(getWindowsWithNewMeld(values.second, wildcards))
        }
        return windows.flatten()
            .map { MeldAttempt(values.first, it - values.third, values.third, it) }
    }

    private fun createWindows(cards: List<PlayingCard>, wildcards: List<PlayingCard>): List<MeldAttempt> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))

        if (cards.isNotEmpty()) {
            windows.addAll(getWindowsWithNewMeld(cards, wildcards))
        }
        return windows.flatten()
            .map { MeldAttempt(handCardsUsed = it) }
    }

    private fun getWindowsWithNewMeld(
        hand: List<PlayingCard>,
        wildcards: List<PlayingCard>,
    ): Collection<List<List<PlayingCard>>> {
        val windows = mutableListOf(listOf(listOf<PlayingCard>()))
        // loop from current size down to meld size + 1
        val minSize = if (wildcards.isEmpty()) 3 else 2

        for (i in hand.size downTo minSize) {
            val handWindows = hand.windowed(i)
            if (i >= 3) windows.add(handWindows) // only add windows of 3 or greater as can be 2 if a wildcard

            if (wildcards.isNotEmpty()) {
                wildcards.forEach { wc ->
                    val handWindowsWithWildcards = handWindows.map { list ->
                        list.toMutableList().apply {
                            add(wc)
                        }.toList()
                    }
                    windows.add(handWindowsWithWildcards)
                }
            }
        }
        return windows
    }

    private fun getWindowsWithExistingMeld(
        values: Triple<Int, List<PlayingCard>, List<PlayingCard>>,
    ): Collection<List<List<PlayingCard>>> {
        val handWildcards = values.second.filter { it.wildcard }
        val allCards = (values.third.union(values.second) + handWildcards).sorted()
        val firstCardIndex = allCards.indexOf(values.third.first())
        val lastCardIndex = allCards.indexOf(values.third.last())

        val windows = mutableListOf(listOf(listOf<PlayingCard>()))
        // loop from current size down to meld size + 1
        for (i in allCards.size downTo values.third.size + 1) {
            val maxSearchDistance = i - values.third.size
            val fromIndex = max(0, firstCardIndex - maxSearchDistance)
            val toIndex = min(allCards.size, lastCardIndex + maxSearchDistance + 1)

            if (fromIndex < toIndex) {
                val subList = allCards.subList(fromIndex, toIndex)
                windows.add(subList.windowed(i))
            }
        }
        return windows
    }
}


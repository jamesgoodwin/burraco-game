package meld

import PlayingCard

interface MeldEvaluator {
    fun getMelds(cards: List<PlayingCard>): List<List<PlayingCard>>
}

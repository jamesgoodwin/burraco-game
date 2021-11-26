package meld

import PlayingCard

data class MeldAttempt(
    val index: Int = -1,
    var handCardsUsed: List<PlayingCard> = emptyList(),
    val existingMeld: List<PlayingCard> = emptyList(),
    val meldCombo: List<PlayingCard> = emptyList()
)
import PlayingCard.Suit.SPADE

data class Meld(val cards: List<PlayingCard>) {

    object CardValues {
        internal val suitValues: List<Float> = PlayingCard.Suit.values().map { it.binaryValue.toFloat() }
        internal val cardValues: List<Float> = PlayingCard.Value.values().map { it.binaryValue.toFloat() }
    }

    private val sumSuits: Int = cards.sumBy { if (it.suit != null) it.suit.binaryValue else 0 }
    private val avgSuit = sumSuits.toFloat() / cards.size.toFloat()
    private val avgSuitMinusSpade = (sumSuits.toFloat() - SPADE.binaryValue) / (cards.size.toFloat() - 1)
    private val avgSuitMinusHeart = (sumSuits.toFloat() - PlayingCard.Suit.HEART.binaryValue) / (cards.size.toFloat() - 1)
    private val avgSuitMinusDiamond = (sumSuits.toFloat() - PlayingCard.Suit.DIAMOND.binaryValue) / (cards.size.toFloat() - 1)
    private val avgSuitMinusClub = (sumSuits.toFloat() - PlayingCard.Suit.CLUB.binaryValue) / (cards.size.toFloat() - 1)

    private val sumValues: Int = cards.sumBy { it.value.binaryValue }
    private val avgValue = sumValues.toFloat() / cards.size.toFloat()
    private val avgValueMinusTwo = (sumValues.toFloat() - PlayingCard.Value.TWO.binaryValue) / (cards.size.toFloat() - 1)
    private val avgValueMinusJoker = (sumValues.toFloat() - PlayingCard.Value.JOKER.binaryValue) / (cards.size.toFloat() - 1)

    fun isAllSameSuite(): Boolean {
        return CardValues.suitValues.any { it == avgSuit || it == avgSuitMinusSpade || it == avgSuitMinusHeart || it == avgSuitMinusDiamond || it == avgSuitMinusClub }
    }

    fun suite(): PlayingCard.Suit? {
        val averages = listOf(avgSuit, avgSuitMinusClub, avgSuitMinusDiamond, avgSuitMinusHeart, avgSuitMinusSpade)
        return PlayingCard.Suit.values().singleOrNull { it.binaryValue.toFloat() in averages } // if combination it wont be possible to determine suit
    }

    fun isSameValue(): Boolean {
        return CardValues.cardValues.any { it == avgValue || it == avgValueMinusTwo || it == avgValueMinusJoker }
    }

}
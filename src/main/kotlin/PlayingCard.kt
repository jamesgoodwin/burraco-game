data class PlayingCard(val value: Value, val suite: Suite? = null) : Comparable<PlayingCard> {

    enum class Suite(val symbol: Char) {
        HEARTS('♥'), DIAMONDS('♦'), CLUB('♣'), SPADES('♠')
    }

    enum class Value(numericValue: Int) {
        JOKER(0),
        ACE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13);
    }

    override fun compareTo(other: PlayingCard): Int {
        return compareValues(this.value, other.value)
    }

}
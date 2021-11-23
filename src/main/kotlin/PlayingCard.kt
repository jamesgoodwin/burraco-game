data class PlayingCard(val value: Value, val suit: Suit? = null) : Comparable<PlayingCard> {

    val wildcard = value == Value.TWO || value == Value.JOKER

    enum class Suit(val symbol: Char) {
        HEART('♥'),
        DIAMOND('♦'),
        CLUB('♣'),
        SPADE('♠')
    }

    enum class Value(val symbol: String, val order: Int) {
        TWO("2", 2),
        THREE("3", 3),
        FOUR("4", 4),
        FIVE("5", 5),
        SIX("6", 6),
        SEVEN("7", 7),
        EIGHT("8", 8),
        NINE("9", 9),
        TEN("10", 10),
        JACK("J", 11),
        QUEEN("Q", 12),
        KING("K", 13),
        ACE("A", 14),
        JOKER("*", 15);

        companion object {
            fun from(order: Int): Value? {
                for(value in values()) {
                    if (value.order == order) return value
                }
                return null
            }
        }
    }

    override fun toString(): String {
        if(value == Value.JOKER) return "Joker"

        return "${value.symbol}${suit?.symbol}"
    }

    override fun compareTo(other: PlayingCard): Int {
        return compareBy<PlayingCard>({ it.suit }, { it.value }).compare(this, other)
    }

}
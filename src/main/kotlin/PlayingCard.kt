data class PlayingCard(val value: Value, val suit: Suit? = null) : Comparable<PlayingCard> {

    enum class Suit(val symbol: Char, val binaryValue: Int) {
        HEART('♥', 1),
        DIAMOND('♦', 2),
        CLUB('♣', 4),
        SPADE('♠', 8)
    }

    enum class Value(val symbol: String, val binaryValue: Int) {
        TWO("2", 1),
        THREE("3", 2),
        FOUR("4", 4),
        FIVE("5", 8),
        SIX("6", 16),
        SEVEN("7", 32),
        EIGHT("8", 64),
        NINE("9", 128),
        TEN("10", 256),
        JACK("J", 512),
        QUEEN("Q", 1024),
        KING("K", 2048),
        ACE("A", 4096),
        JOKER("*", 8192);
    }

    override fun toString(): String {
        if(value == Value.JOKER) {
            return "Joker"
        }
        return "${value.symbol}${suit?.symbol}"
    }

    override fun compareTo(other: PlayingCard): Int {
        return compareBy<PlayingCard>({ it.suit }, { it.value }).compare(this, other)
    }

}
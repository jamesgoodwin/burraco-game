class Deck() {
    fun shuffle() : List<PlayingCard> {
        val cards = ArrayList<PlayingCard>()

        for (suit in PlayingCard.Suit.values()) {
            for (value in PlayingCard.Value.values().filterNot { it == PlayingCard.Value.JOKER }) {
                repeat(2) {
                    cards.add(PlayingCard(value, suit))
                }
            }
        }

        repeat(4) {
            cards.add(PlayingCard(PlayingCard.Value.JOKER))
        }

        cards.shuffle()
        return cards
    }
}
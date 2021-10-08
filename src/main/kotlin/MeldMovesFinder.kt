import PlayingCard.Suit

class MeldMovesFinder(val meldValidator: MeldValidator) {

    fun findMoves(hand: List<PlayingCard>, state: State, melds: Map<Suit, List<PlayingCard>>): List<Move>? {
        val sequenceMoves = (melds.asSequence() + hand.groupBy { it.suit }
            .filterKeys { key -> melds.containsKey(key) }.asSequence())
//            .distinct()
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, values) -> values.flatten() }
            .filterValues { meldValidator.isValid(it) }

        return sequenceMoves.mapValues { NewMeldMove(meldValidator, it.value, state) }.values.toList()
    }


}
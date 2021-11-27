import meld.Meld

interface MovesFinder {

    fun getAllMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): List<MeldMove>
}

import meld.Meld
import meld.MeldMove

interface MovesFinder {

    fun getAllMoves(hand: List<PlayingCard>, state: State, melds: List<Meld>): Set<MeldMove>
}

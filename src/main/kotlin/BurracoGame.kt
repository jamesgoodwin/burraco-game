import PlayingCard.Value.JOKER
import meld.HandEvaluator
import meld.MeldMovesFinder
import player.HumanPlayer
import player.IsmctsPlayer
import player.LowAiPlayer
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

class BurracoGame(
    var state: State = State(
        players = listOf(HumanPlayer("1"), IsmctsPlayer()),
        meldMovesFinder = MeldMovesFinder()),
) {

    init {
        dealCards()
    }

    fun runGame() {
        while (!state.finished) {
            val name = state.playersTurn.name()
            println("--- player.Player ($name) turn ---")
            state.printGameState(state.playersTurn)
            state.takeNextTurn()
        }
    }

    private fun dealCards() {
        var cards = ArrayList<PlayingCard>()

        for (suit in PlayingCard.Suit.values()) {
            for (value in PlayingCard.Value.values().filterNot { it == JOKER }) {
                repeat(2) {
                    cards.add(PlayingCard(value, suit))
                }
            }
        }

        repeat(4) {
            cards.add(PlayingCard(JOKER))
        }

        cards.shuffle()

        for (player in state.players) {
            val pot = ArrayList<PlayingCard>(cards.take(11))
            cards = ArrayList(cards.subList(11, cards.size))
            state.pots[player]?.addAll(pot)

            val hand = ArrayList<PlayingCard>(cards.take(11))
            cards = ArrayList(cards.subList(11, cards.size))
            state.hands[player]?.addAll(hand)
        }

        state.stock = ArrayDeque(cards)
    }

}
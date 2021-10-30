import PlayingCard.Value.JOKER
import java.lang.RuntimeException
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

class BurracoGame(var state: State = State(
    listOf(
        HumanPlayer("1"),
        LowAiPlayer(meldEvaluator = HandEvaluator())
    )
)
) {

    init {
        dealCards()
    }

    fun runGame() {
        while (!state.finished) {
            val name = state.playersTurn.name()
            println("--- Player ($name) turn ---")
            state.printGameState(state.playersTurn)
            state.playersTurn.takeTurn(state, StateBasedPlayerTurn(state))
            nextPlayer()
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

    private fun nextPlayer() {
        state.playersTurn = when (state.playersTurn) {
            state.players[0] -> state.players[1]
            state.players[1] -> state.players[0]
            else -> throw RuntimeException("Unknown player")
        }
    }

    private fun clear() {
        state = State(listOf(HumanPlayer("1"), HumanPlayer("2")))
    }
}
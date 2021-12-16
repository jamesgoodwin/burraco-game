import meld.HandEvaluator
import meld.MeldMovesFinder
import player.HumanPlayer
import player.LowAiPlayer

class BurracoGame(
    var state: State = State(
        players = listOf(HumanPlayer("1"), LowAiPlayer(HandEvaluator())),
        meldMovesFinder = MeldMovesFinder()),
) {

    init {
        dealCards()
    }

    fun runGame() {
        while (!state.finished) {
            val name = state.playersTurn.name()
            println("--- Player ($name) turn ---")
            state.printGameState(state.playersTurn)
            state.takeNextTurn()
        }
    }

    private fun dealCards() {
        var cards = Deck().shuffle()

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
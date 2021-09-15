import PlayingCard.Value.JOKER
import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

class BurracoGame(var state: State = State(listOf(HumanPlayer("1"), HumanPlayer("2")))) {

    init {
        dealCards()
    }

    fun runGame() {
        while (!state.finished) {
            nextPlayer()
            state.playersTurn.takeTurn(state, StateBasedPlayerTurn(state, FastMeldValidator()))
        }
    }

//    fun discard(player: HumanPlayer, cards: PlayingCard) {
//        checkPlayersTurnAndTakenCards(player)
//
//        state.discard.add(cards)
//        player.hand.remove(cards)
//        nextPlayer()
//    }
//
//    fun takeCard(player: HumanPlayer) {
//        checkPlayersTurnAndNotTakenCards(player)
//
//        state.playerTakenCards = true
//        player.hand.add(state.stock.removeFirst())
//    }
//
//    fun meld(player: HumanPlayer, cards: List<PlayingCard>, index: Int?) {
//        checkPlayersTurnAndTakenCards(player)
//
//        val cardsToMeld = if (index != null && index < player.melds.size) {
//            player.melds[index] + cards
//        } else {
//            cards
//        }
//
//        if (meldValidator.isValid(cardsToMeld)) {
//            player.melds.add(cardsToMeld.toMutableList())
//            player.hand.removeAll(cardsToMeld)
//        }
//    }

    private fun checkPlayersTurnAndTakenCards(player: HumanPlayer) {
        checkPlayersTurn(player)
        if (!state.playerTakenCards)
            throw UnsupportedOperationException("Must take a card before discarding!")
    }

    private fun checkPlayersTurnAndNotTakenCards(player: HumanPlayer) {
        checkPlayersTurn(player)
        if (state.playerTakenCards)
            throw UnsupportedOperationException("Already taken a card!")
    }

    private fun checkPlayersTurn(player: HumanPlayer) {
        if (state.playersTurn != player)
            throw UnsupportedOperationException("Not your turn!")
    }

    private fun dealCards() {
        var cards = ArrayList<PlayingCard>()

        for (suite in PlayingCard.Suit.values()) {
            cards.add(PlayingCard(JOKER))
            for (value in PlayingCard.Value.values()) {
                if (value == JOKER) continue
                repeat(2) {
                    cards.add(PlayingCard(value, suite))
                }
            }
        }

        cards.shuffle()

        for(player in state.players) {
            val pot = ArrayList<PlayingCard>(cards.take(11))
            cards = ArrayList(cards.subList(11, cards.size))
            state.pots[player]?.addAll(pot)

            val hand = ArrayList<PlayingCard>(cards.take(11))
            cards = ArrayList(cards.subList(11, cards.size))
            state.hands[player]?.addAll(pot)
        }

        state.stock = ArrayDeque(cards)
    }

    private fun nextPlayer() {
        state.playersTurn = when (state.playersTurn) {
            state.players[0] -> state.players[1]
            state.players[1] -> state.players[0]
            else -> throw RuntimeException("Unknown player")
        }
        state.playerTakenCards = false
    }

    private fun clear() {
        state = State(listOf(HumanPlayer("1"), HumanPlayer("2")))
    }
}
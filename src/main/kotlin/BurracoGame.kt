import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

class BurracoGame(playingMode: PlayingMode, val state: State = State()) {

    private val meldValidator = MeldValidator()

    init {
        deal()
    }

    fun deal() {
        clear()
        dealCards()
    }

    fun discard(player: Player, cards: PlayingCard) {
        checkPlayersTurnAndTakenCards(player)

        state.discard.add(cards)
        player.hand.remove(cards)
        nextPlayer()
    }

    fun takeCard(player: Player) {
        checkPlayersTurnAndNotTakenCards(player)

        state.playerTakenCards = true
        player.hand.add(state.stock.removeFirst())
    }

    fun meld(player: Player, meld: List<PlayingCard>) {
        checkPlayersTurnAndTakenCards(player)

        if(meldValidator.isValidMeld(meld)) {
            player.melds.add(meld.toMutableList())
            player.hand.removeAll(meld)
        }
    }

    private fun checkPlayersTurnAndTakenCards(player: Player) {
        checkPlayersTurn(player)
        if (!state.playerTakenCards)
            throw UnsupportedOperationException("Must take a card before discarding!")
    }

    private fun checkPlayersTurnAndNotTakenCards(player: Player) {
        checkPlayersTurn(player)
        if (state.playerTakenCards)
            throw UnsupportedOperationException("Already taken a card!")
    }

    private fun checkPlayersTurn(player: Player) {
        if (state.playersTurn != player)
            throw UnsupportedOperationException("Not your turn!")
    }

    private fun dealCards() {
        var cards = ArrayList<PlayingCard>()
        for (suite in PlayingCard.Suite.values()) {
            cards.add(PlayingCard(PlayingCard.Value.JOKER))
            for (value in PlayingCard.Value.values()) {
                if (value == PlayingCard.Value.JOKER) continue
                repeat(2) {
                    cards.add(PlayingCard(value, suite))
                }
            }
        }
        cards.shuffle()
        repeat(2) {
            val pot = ArrayList<PlayingCard>(cards.take(11))
            cards = ArrayList(cards.subList(11, cards.size))
            state.pots.add(pot)
        }

        for (player in state.players) {
            val pot = ArrayList<PlayingCard>(cards.take(11))
            cards = ArrayList(cards.subList(11, cards.size))
            player.hand.addAll(pot)
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
        state.playerTakenCards = false
        state.stock.clear()
        state.discard.clear()
        state.pots.clear()

        for (player in state.players) {
            player.hand.clear()
            player.melds.clear()
        }
    }
}
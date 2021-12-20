import meld.Burraco
import meld.Burraco.*
import meld.Meld
import meld.MeldMove
import player.Player
import player.PlayerTurnState.*
import java.lang.RuntimeException

data class State(
    val players: List<Player>,
    val meldMovesFinder: MovesFinder,
    var playersTurn: Player = players[0],
    var stock: ArrayDeque<PlayingCard> = ArrayDeque(),
    val discard: MutableList<PlayingCard> = mutableListOf(),
) : Cloneable {

    constructor(state: State) : this(state.players,
        state.meldMovesFinder,
        state.playersTurn,
        state.stock,
        state.discard.toMutableList())

    var finished: Boolean = false
    var playerTurnState: StateBasedPlayerTurn = StateBasedPlayerTurn(this)

    val melds: Map<Player, MutableList<MutableList<PlayingCard>>> = players.associateWith { mutableListOf() }

    val hands: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }
    val pots: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }

    // a players cards which can be considered seen by the other players
    val seenCards: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }

    fun hand(player: Player): MutableList<PlayingCard> {
        return hands[player] ?: mutableListOf()
    }

    fun melds(player: Player): MutableList<MutableList<PlayingCard>> {
        return melds[player] ?: mutableListOf(mutableListOf())
    }

    fun roundOver(): Boolean = playerTurnState.roundOver()

    public override fun clone(): State {
        val clone = State(state = this)
        clone.playersTurn = this.playersTurn
        clone.playerTurnState = this.playerTurnState.clone(clone)
        clone.stock = ArrayDeque()
        clone.stock.addAll(this.stock)

        clone.pots.forEach {
            val cards = this.pots[it.key]
            if (cards != null) {
                it.value.addAll(cards)
            }
        }

        clone.melds.forEach {
            val melds = this.melds[it.key]
            if (melds != null) {
                it.value.addAll(melds.toList().map { cards -> cards.toMutableList() })
            }
        }

        clone.hands.forEach {
            val hand = this.hands[it.key]
            if (hand != null) {
                it.value.addAll(hand.toList())
            }
        }

        clone.seenCards.forEach {
            val cards = this.seenCards[it.key]
            if (cards != null) {
                it.value.addAll(cards)
            }
        }

        assert(clone.totalCardCount() <= 108)
        return clone
    }

    fun printTotalCardCount() {
        val cardsInHands = players.map { hand(it) }.map { it.size }.sumOf { it }
        val cardsInMelds = players.map { melds(it) }.flatten().map { it.size }.sumOf { it }
        val cardsInPot = players.mapNotNull { pots[it] }.map { it.size }.sumOf { it }
        val totalCards = cardsInHands + cardsInMelds + cardsInPot + stock.size + discard.size

        println("T: $totalCards, H: $cardsInHands, M: $cardsInMelds, P:$cardsInPot, S:${stock.size}, D:${discard.size}")
    }

    private fun totalCardCount(): Int {
        val cardsInHands = players.map { hand(it) }.map { it.size }.sumOf { it }
        val cardsInMelds = players.map { melds(it) }.flatten().map { it.size }.sumOf { it }
        val cardsInPot = players.mapNotNull { pots[it] }.map { it.size }.sumOf { it }

        return cardsInHands + cardsInMelds + cardsInPot + stock.size + discard.size
    }

    private fun pileToString(): String {
        if (discard.isNotEmpty()) {
            val cards = discard.joinToString(", ")
            return ("Discard pile: $cards")
        }
        return ("Discard pile:[]")
    }

    fun meldsToString(player: Player): String {
        val melds = melds(player)
        if (melds.isNotEmpty()) {
            val cards = melds.joinToString(" - ") { it.sorted().joinToString(", ") }
            return "Melds: $cards"
        }
        return "Melds: []"
    }

    private fun handToString(player: Player): String {
        val cards = hand(player).sorted()
        val hand = cards.joinToString(", ")
        return ("Hand: $hand")
    }

    fun printGameState(player: Player) {
        println(pileToString())
        println(handToString(player))
        println(meldsToString(player))
        println(pointsToString(player))
    }

    private fun pointsToString(player: Player): String {
        val points = points(player)
        return "Points: $points"
    }

    fun hasBurraco(player: Player): Boolean {
        return melds[player]?.any { it.size >= 7 } == true
    }

    fun playerClosed(player: Player): Boolean {
        return pots[player]?.isEmpty() == true && hands[player]?.isEmpty() == true && hasBurraco(player)
    }

    fun points(player: Player): Int {
        val burracoPoints = getBurracoPoints(player)
        val meldCardPoints = melds[player]?.sumOf { it.sumOf { card -> card.value.points } } ?: 0

//        val handCardPoints = hands[player]?.sumOf { it.value.points } ?: 0
//        val takenPot = pots[player]?.isEmpty()
//        val takenPotPenalty = if (takenPot == true) 0 else 100

        val playerHasWon = finished && playerClosed(player)
        val winningPoints = if (playerHasWon) 100 else 0

        return burracoPoints + meldCardPoints + winningPoints
    }

    fun ismctsScore(player: Player): Double {
        val burracos = burracos(player)
        var score = 0.0

        if (burracos.isNotEmpty()) {
            score += 0.3
        }

        if (pots[player]?.isEmpty() == true) {
            score += 0.4
        }

        // 0.3 left to win
        val handCardsSize = hand(player).size

        score += when {
            handCardsSize >= 10 -> 0.05
            handCardsSize in 7..10 -> 0.1
            handCardsSize in 5..7 -> 0.15
            handCardsSize in 3..5 -> 0.2
            handCardsSize in 2..3 -> 0.25
            handCardsSize == 1 -> 0.3
            else -> 0.0
        }

        return score
    }

    private fun getBurracoPoints(player: Player): Int {
        return burracos(player).map {
            when (it) {
                PULITO -> 200
                SEMI_PULITO -> 150
                SPORCO -> 100
                else -> 0
            }
        }.sum()
    }

    fun burracos(player: Player): List<Burraco> {
        val melds = melds[player]?.filter { it.size >= 7 }

        return if (melds?.isNotEmpty() == true) {
            melds.map { Meld(it).getBurracoType() }.filterNot { it == NO_BURRACO }
        } else emptyList()
    }

    fun getAllPossibleMoves(): List<Move> {
        if(stock.isEmpty()) return emptyList()

        return when (playerTurnState.getTurnState()) {
            PICKING -> listOf(TakePileMove(), TakeCardMove())
            MELDING -> {
                val hand = hand(playersTurn)
                val allMoves: MutableList<Move> = getMeldMoves(hand).toMutableList()
                allMoves.addAll(hand.map { DiscardMove(it) })
                allMoves
            }
            FINISHED -> emptyList()
        }
    }

    private fun getMeldMoves(hand: MutableList<PlayingCard>): Set<MeldMove> {
        val melds = melds(playersTurn).map { cards -> getMeldFast(cards) }
        return meldMovesFinder.getAllMoves(hand, this, melds)
    }

    val getMeldFast = this::getMeldSlow.memoize(5000)

    private fun getMeldSlow(cards: List<PlayingCard>): Meld {
        return Meld(cards)
    }

    internal fun advancePlayer() {
        playersTurn = when (playersTurn) {
            players[0] -> players[1]
            players[1] -> players[0]
            else -> throw RuntimeException("Unknown player")
        }
        playerTurnState = StateBasedPlayerTurn(this)
    }

    fun doMove(moveToPlay: Move?) {
        if (moveToPlay != null) {
            playerTurnState.performMove(moveToPlay)
        }
    }

    fun takeNextTurn() {
        playersTurn.takeTurn(this, playerTurnState)
        if (playerClosed(playersTurn)) {
            finished = true
        }
        // todo - finish the game if less cards in the deck than full player rounds
    }

    fun cloneAndRandomiseState(): State {
        val clone = this.clone()

        // get a copy of all the seen cards
        val visibleCards = clone.seenCards.toMutableMap()
        visibleCards[playersTurn]?.clear()
        visibleCards[playersTurn]?.addAll(hand(playersTurn))

        // get the deck, pots and any unseen cards from each player, add them together and shuffle
        val hiddenCards = ArrayDeque(getHiddenCards(clone, visibleCards).shuffled())

        // loop through each player
        clone.players.forEach { player ->
            val hand = this.hands[player]
            val handSize = hand?.size ?: 0
            hand?.clear()
            hand?.addAll(visibleCards[player] ?: emptyList())
            val remaining = handSize - (hand?.size ?: 0)
            val cardsToBeAdded = hiddenCards.take(remaining)
            cardsToBeAdded.forEach { hiddenCards.remove(it) }
            hand?.addAll(cardsToBeAdded)
        }

        clone.pots.mapValues { (_, cards) ->
            val count = cards.size
            cards.clear()
            val cardsToBeAdded = hiddenCards.take(count)
            cardsToBeAdded.forEach { hiddenCards.remove(it) }
            cards.addAll(cardsToBeAdded)
        }

        assert(clone.totalCardCount() <= 108)

        clone.stock.clear()
        clone.stock = hiddenCards

        assert(clone.totalCardCount() <= 108)

        return clone
    }

    private fun getHiddenCards(
        clone: State,
        allVisibleCards: MutableMap<Player, MutableList<PlayingCard>>,
    ): List<PlayingCard> {
        val allCards = clone.stock.toMutableList()
        allCards += clone.pots.flatMap { it.value }.toList()

        hands.forEach { (player, cards) ->
            val list = cards.toMutableList()
            allVisibleCards[player]?.forEach {
                list.remove(it)
            }
            allCards.addAll(list)
        }

        return allCards
    }

    fun takePile(player: Player) {
        hands[player]?.addAll(discard)
        seenCards[player]?.addAll(discard)
        discard.clear()
    }

    fun takePot(player: Player) {
        pots[player]?.toMutableList()?.let { hands[player]?.addAll(it) }
        pots[player]?.clear()
    }

    fun takeCard(player: Player) {
//        print("Take card - remaining ${stock.size}")
        hands[player]?.add(stock.removeLast())
    }

    fun discardCard(player: Player, card: PlayingCard) {
        seenCards[player]?.remove(card)
        discard.add(card)
        hands[player]?.remove(card)
    }

    fun meld(cards: List<PlayingCard>, index: Int): Boolean {
        if (!hand(playersTurn).containsAll(cards)) return false

        val existingMeld = melds(playersTurn)[index]
        existingMeld.addAll(cards)
        cards.forEach { card ->
            hands[playersTurn]?.remove(card)
        }
        return true
    }

    fun meld(player: Player, cards: List<PlayingCard>, valid: Boolean): Boolean {
        if (!hand(player).containsAll(cards)) return false
        if (valid) {
            melds[player]?.add(cards.toMutableList())
            cards.forEach { card ->
                hands[player]?.remove(card)
                seenCards[player]?.remove(card)
            }
            return true
        }
        return false
    }

    override fun toString(): String {
        return super.toString()
    }

}

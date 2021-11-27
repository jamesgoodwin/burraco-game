import meld.Burraco
import meld.Burraco.*
import meld.Meld
import player.Player
import player.PlayerTurnState.MELDING
import player.PlayerTurnState.PICKING

class State(val players: List<Player>, val meldMovesFinder: MovesFinder) {

    var finished: Boolean = false
    var playersTurn: Player = players[0]
    var playerTurnState = PICKING

    val hands: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }
    val melds: Map<Player, MutableList<MutableList<PlayingCard>>> = players.associateWith { mutableListOf() }

    var stock = ArrayDeque<PlayingCard>()
    val discard = mutableListOf<PlayingCard>()
    val pots: Map<Player, MutableList<PlayingCard>> = players.associateWith { mutableListOf() }

    fun hand(player: Player): MutableList<PlayingCard> {
        return hands[player] ?: mutableListOf()
    }

    fun melds(player: Player): MutableList<MutableList<PlayingCard>> {
        return melds[player] ?: mutableListOf(mutableListOf())
    }

    fun printTotalCardCount() {
        val cardsInHands = players.mapNotNull { hand(it) }.map { it.size }.sumOf { it }

        val cardsInMelds = players.mapNotNull { melds(it) }.flatten().map { it.size }.sumOf { it }

        val cardsInPot = players.mapNotNull { pots[it] }.map { it.size }.sumOf { it }

        val totalCards = cardsInHands + cardsInMelds + cardsInPot + stock.size + discard.size
        println("T: $totalCards, H: $cardsInHands, M: $cardsInMelds, P:$cardsInPot, S:${stock.size}, D:${discard.size}")
    }

    private fun printPile() {
        if (discard.isNotEmpty()) {
            val cards = discard.joinToString(", ")
            println("Discard pile: $cards")
        }
    }

    fun printMelds(player: Player) {
        val melds = melds(player)
        if (melds.isNotEmpty()) {
            val cards = melds.joinToString(" - ") { it.sorted().joinToString(", ") }
            println("Melds: $cards")
        }
    }

    private fun printHand(player: Player) {
        val cards = hand(player).sorted()
        val hand = cards.joinToString(", ")
        println("Hand: $hand")
    }

    fun printGameState(player: Player) {
        printPile()
        printHand(player)
        printMelds(player)
        printPoints(player)
    }

    private fun printPoints(player: Player) {
        val points = points(player)
        println("Points: $points")
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

        val handCardPoints = hands[player]?.sumOf { it.value.points } ?: 0
        val takenPot = pots[player]?.isEmpty()
        val takenPotPenalty = if (takenPot == true) 0 else 100

        val playerHasWon = finished && playerClosed(player)
        val winningPoints = if(playerHasWon) 100 else 0

        return ((burracoPoints + meldCardPoints + winningPoints) - handCardPoints) - takenPotPenalty
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
        return when(playerTurnState) {
            PICKING -> listOf(TakePileMove(this), TakeCardMove(this))
            MELDING -> {
                val hand = hand(playersTurn)
                val melds = melds(playersTurn).map { cards -> Meld(cards) }
                meldMovesFinder.getAllMoves(hand, this, melds)
            }
        }
    }

}


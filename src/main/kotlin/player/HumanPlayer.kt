package player

import meld.Meld
import meld.MeldMovesFinder
import State

class HumanPlayer(private val name: String) : Player {

    private val meldMovesFinder = MeldMovesFinder()

    override fun takeTurn(state: State, turn: PlayerTurn) {
        takeCardInput(turn)

        state.printGameState(this)

        placeCardInput(state, turn)
    }

    override fun name(): String = name

    private fun takeCardInput(turn: PlayerTurn) {
        println("Please choose an option:")
        println("1. to pick up from deck\n2. to pick up from pile")

        readLine().let {
            when (it) {
                "1" -> turn.takeCard()
                "2" -> turn.takePile()
                else -> {
                    println("Error!")
                    takeCardInput(turn)
                }
            }
        }
    }

    private fun placeCardInput(state: State, turn: PlayerTurn) {
        val handState = state.hand(this)
        val meldsState = state.melds(this)?.map { cards -> Meld(cards) }

        val moves = when {
            (handState != null && meldsState != null) -> meldMovesFinder.getAllMoves(handState, state, meldsState)
            handState != null -> meldMovesFinder.getAllMoves(handState, state, emptyList())
            else -> null
        }

        println("Please choose an option:")
        if (moves?.isNotEmpty() == true) {
            println("1. to discard a card\n2. to meld cards")
        } else {
            println("1. to discard a card")
        }

        readLine().let {
            when (it) {
                "1" -> {
                    discardCardInput(state, turn)
                }
                "2" -> {
                    println("Choose cards to meld")
                    moves?.forEachIndexed { index, move ->
                        println("${index + 1}. $move")
                    }
                    readLine()?.let { meldIndex ->
                        val move = moves?.get(meldIndex.toInt() - 1)
                        if (move != null && turn.meld(move)) {
                            state.printGameState(this)
                            placeCardInput(state, turn)
                        } else {
                            println("Error")
                            placeCardInput(state, turn)
                        }
                    }
                }
                else -> {
                    println("Error")
                    placeCardInput(state, turn)
                }
            }
        }
    }

    private fun discardCardInput(state: State, turn: PlayerTurn) {
        println("Enter a card to discard")
        readLine()?.let { index ->
            if (index.toInt() > 0) {
                val card = state.hand(this)?.sorted()?.get(index.toInt() - 1)
                if (card != null) {
                    turn.discard(card)
                }
            }
        }
    }

}

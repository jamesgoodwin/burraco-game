class HumanPlayer(val name: String) : Player {

    //todo - remove constructor args

    override fun takeTurn(state: State, turn: PlayerTurn) {
        println("--- Player ($name) turn ---")

        printGameState(state)

        println("Please choose an option:")
        println("1. to pick up from deck\n2. to pick up from pile")

        readLine().let {
            when (it) {
                "1" -> turn.takeCard()
                "2" -> turn.takePile()
                else -> print("Unexpected input!")
            }
        }

        printGameState(state)
        placeCardInput(state, turn)
    }

    private fun printGameState(state: State) {
        printPile(state)
        printHand(state)
        printMelds(state)
    }

    private fun printPile(state: State) {
        if (state.discard.isNotEmpty()) {
            val cards = state.discard.joinToString(", ")
            println("Discard pile: $cards")
        }
    }

    private fun printMelds(state: State) {
        val melds = state.melds(this)
        if (melds?.isNotEmpty() == true) {
            val cards = melds.joinToString(" - ") { it.sorted().joinToString(", ") }
            println("Melds: $cards")
        }
    }

    private fun placeCardInput(state: State, turn: PlayerTurn) {
        println("Please choose an option:")
        println("1. to discard a card\n2. to meld cards")

        readLine().let {
            when (it) {
                "1" -> {
                    discardCardInput(state, turn)
                }
                "2" -> {
                    println("Enter a list of cards to meld")
                    readLine().let { cards ->
                        val hand = state.hand(this)?.sorted()
                        val result = cards?.split(",")
                            ?.map { index -> Integer.valueOf(index) - 1 }
                            ?.mapNotNull { index -> hand?.elementAt(index) }
                        if (result != null) {
                            if (turn.meld(result, 0)) {
                                printGameState(state)
                                discardCardInput(state, turn)
                            } else {
                                println("Error")
                                placeCardInput(state, turn)
                            }
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
                val card = state.hand(this)?.sorted()?.get(index.toInt()-1)
                if (card != null) {
                    turn.discard(card)
                }
            }
        }
    }

    private fun printHand(state: State) {
        val cards = state.hand(this)?.sorted()
        val hand = cards?.joinToString(", ")
        println("Hand: $hand")
    }

//    fun takeCard(game: BurracoGame) {
//        game.takeCard(this)
//    }
//
//    fun discard(game: BurracoGame) {
//        game.discard(this, state.hand[0])
//    }
//
//    fun meld(game: BurracoGame, meld: List<PlayingCard>, index: Int? = null) {
//        game.meld(this, meld, index)
//    }

}

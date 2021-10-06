class HumanPlayer(private val name: String) : Player {

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
                                state.printGameState(this)
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

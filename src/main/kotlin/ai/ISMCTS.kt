package ai

import Move
import State

class ISMCTS(
    private val rootState: State,
    private val limit: Int,
) {

    private val rootNode: Node = Node(player = rootState.playersTurn)

    private var currentNode: Node? = rootNode
    private lateinit var currentState: State
    private lateinit var possibleMoves: List<Move>

    companion object {
        const val EXPLORATION_FACTOR: Double = 0.7
    }

    fun run(): Move? {
        for (i in 0 until limit) {
            currentNode = rootNode

            // 1. Clone and determine the state of the game (randomize information unknown to the AI)
            currentState = rootState.cloneAndRandomiseState()
            possibleMoves = currentState.getAllPossibleMoves()

            selectChildISMCTS()
            expandTreeISMCTS() // change currentNode from root to child
            simulateISMCTS()
            backPropagateISMCTS()
        }

        val best = rootNode.children.maxByOrNull { it.visits }
        return best?.moveToPlay
    }

    private fun expandTreeISMCTS() {
        val untriedMoves = currentNode?.getUntriedMoves(possibleMoves)
        if (untriedMoves?.isNotEmpty() == true) {
            val randomMove = untriedMoves.random()
            val currentPlayer = currentState.playersTurn
            if (this.possibleMoves.size == 1) {
                currentState.doMove(randomMove)
                selectChildISMCTS()
                expandTreeISMCTS()
            } else {
                currentNode = currentNode?.addChild(randomMove, currentPlayer)
                currentState.doMove(randomMove)
            }
        }
    }

    private fun selectChildISMCTS() {
        // can go in infinite loop if selectChild returns null
        while ((possibleMoves.isNotEmpty()
                    && !this.currentState.roundOver())
            && currentNode?.getUntriedMoves(this.possibleMoves)?.isEmpty() == true
        ) { // While every move option has been explored and the game hasn't ended
            val child = currentNode?.selectChild(possibleMoves, EXPLORATION_FACTOR)
            if (child != null) {
                currentNode = child
                currentState.doMove(currentNode?.moveToPlay)
            }
            possibleMoves = currentState.getAllPossibleMoves()
        }
    }

    private fun simulateISMCTS() {
        possibleMoves = currentState.getAllPossibleMoves()
        currentState.playersTurn.name()

        while (possibleMoves.isNotEmpty() && !currentState.roundOver()) {
            // Do the random move and update possible moves
            currentState.doMove(possibleMoves.random())
            possibleMoves = currentState.getAllPossibleMoves()
        }
    }

    private fun backPropagateISMCTS() {
        val score = currentState.points(currentState.playersTurn)

        while (currentNode != null) {
            currentNode?.update(currentState.playersTurn, score.toLong())
            currentNode = currentNode?.parent
        }
    }

}
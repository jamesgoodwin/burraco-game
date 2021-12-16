package ai

import Move
import State

class ISMCTS(
    private val rootState: State,
    private val limit: Int,
) {

    private val rootNode: Node = Node(player = rootState.playersTurn)

    private var currentNode : Node? = rootNode
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
            expandTreeISMCTS()
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
                currentState.doMove(randomMove, false)
                selectChildISMCTS()
                expandTreeISMCTS()
            } else {
                currentNode?.addChild(randomMove, currentPlayer)
                currentState.doMove(randomMove, false)
            }
        }
    }

    private fun selectChildISMCTS() {
        while (possibleMoves.isNotEmpty()
            && !currentState.finished
            && currentNode?.getUntriedMoves(possibleMoves)?.isNotEmpty() == true
            && currentNode?.children?.isNotEmpty() == true) {
            val child = currentNode?.selectChild(possibleMoves, EXPLORATION_FACTOR)
            if (child != null) {
                currentNode = child
                currentState.doMove(currentNode?.moveToPlay, true)
            }
            possibleMoves = currentState.getAllPossibleMoves()
        }
    }

    private fun simulateISMCTS() {
        possibleMoves = currentState.getAllPossibleMoves()
        currentState.playersTurn.name()

        while (possibleMoves.isNotEmpty() && !currentState.roundOver()) {
            // Do the random move and update possible moves
            currentState.doMove(possibleMoves.random(), false)
            possibleMoves = currentState.getAllPossibleMoves()
        }
    }

    private fun backPropagateISMCTS() {
        val winResult = currentState.points(currentState.playersTurn)

        while (currentNode != null) {
            currentNode?.update(currentState.playersTurn, winResult.toLong())
            currentNode = currentNode?.parent
        }
    }

}
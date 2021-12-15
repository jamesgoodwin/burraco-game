package ai

import Move
import State


class ISMCTS(
    private val rootState: State,
    private val limit: Int,
) {

    private val rootNode: Node = Node(player = rootState.playersTurn)

    private var currentNode = rootNode
    private lateinit var currentState: State
    private lateinit var possibleMoves: List<Move>

    companion object {
        const val EXPLORATION_FACTOR: Double = 0.7
    }

    fun run(): Move? {
        for (i in 0 until limit) {
            currentNode = rootNode

            // 1. Clone and determinize the state of the game (randomize information unknown to the AI)
            currentState = rootState.cloneAndRandomiseState()

            possibleMoves = currentState.getAllPossibleMoves()

            selectChildISMCTS()

            expandTreeISMCTS()
        }

        val best = rootNode.children.maxByOrNull { it.visits }
        return best?.moveToPlay
    }

    private fun expandTreeISMCTS() {
        val untriedMoves = currentNode.getUntriedMoves(possibleMoves)
        if (untriedMoves.isNotEmpty()) {
            val randomMove = untriedMoves.random()
            val currentPlayer = currentState.playersTurn
            if (this.possibleMoves.size == 1) {
                currentState.doMove(randomMove, false)
                selectChildISMCTS()
                expandTreeISMCTS()
            } else {
                currentNode.addChild(randomMove, currentPlayer)
                currentState.doMove(randomMove, false)
            }
        }
    }

    private fun selectChildISMCTS() {
        while (possibleMoves.isNotEmpty()
            && !currentState.finished
            && currentNode.getUntriedMoves(possibleMoves).isNotEmpty()
            && currentNode.children.isNotEmpty()) {
            val child = currentNode.selectChild(possibleMoves, EXPLORATION_FACTOR)
            if (child != null) {
                currentNode = child
                currentState.doMove(currentNode.moveToPlay, true)
            }
            possibleMoves = currentState.getAllPossibleMoves()
        }
    }


}
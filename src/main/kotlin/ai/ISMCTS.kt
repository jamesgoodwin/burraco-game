package ai

import Move
import State
import kotlin.system.measureTimeMillis

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

    var selectChildTime = 0L
    var expandTreeTime = 0L
    var simulateTime = 0L
    var backPropagateTime = 0L
    var getMovesTime = 0L

    fun run(): Move? {
        for (i in 0 until limit) {
            currentNode = rootNode

            // 1. Clone and determine the state of the game (randomize information unknown to the AI)
            currentState = rootState.cloneAndRandomiseState()
            possibleMoves = currentState.getAllPossibleMoves()

            selectChildTime += measureTimeMillis { selectChildISMCTS() }
            expandTreeTime += measureTimeMillis { expandTreeISMCTS() } // change currentNode from root to child
            simulateTime += measureTimeMillis { simulateISMCTS() }
            backPropagateTime += measureTimeMillis { backPropagateISMCTS() }
        }

        println("TIME: selectChildTime: $selectChildTime ms, expandTreeTime: $expandTreeTime ms, " +
                "simulateTime: $simulateTime ms, backPropagateTime: $backPropagateTime ms," +
                "getMovesTime: $getMovesTime ms")

        val best = rootNode.children.maxByOrNull { it.visits }
        return best?.moveToPlay
    }

    private fun expandTreeISMCTS() {
        val untriedMoves = currentNode?.getUntriedMoves(possibleMoves)
        if (untriedMoves?.isNotEmpty() == true) {
            val randomMove = untriedMoves.random()
            val currentPlayer = currentState.playersTurn
            currentNode = currentNode?.addChild(randomMove, currentPlayer)
            currentState.doMove(randomMove)
        }
    }

    private fun selectChildISMCTS() {
        while ((possibleMoves.isNotEmpty() && !this.currentState.finished)
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
        getMovesTime += measureTimeMillis { possibleMoves = currentState.getAllPossibleMoves() }
        currentState.playersTurn.name()

        var simulationDepth = 0
        while (possibleMoves.isNotEmpty() && !currentState.finished) {
            // Do the random move and update possible moves
            currentState.doMove(possibleMoves.random())
            if(currentState.roundOver()) currentState.advancePlayer()
            getMovesTime += measureTimeMillis { possibleMoves = currentState.getAllPossibleMoves() }
            simulationDepth++
//            println("Simulation depth: $simulationDepth, player: ${currentState.playersTurn}")
        }
    }

    private fun backPropagateISMCTS() {
        val score = currentState.ismctsScore(currentState.playersTurn)

        while (currentNode != null) {
            currentNode?.update(currentState.playersTurn, score)
            currentNode = currentNode?.parent
        }
    }

}
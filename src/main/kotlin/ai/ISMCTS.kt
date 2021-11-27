package ai

import Move
import State

class ISMCTS(
    private val rootState: State,
    private val limit: Int,
) {

    private lateinit var currentNode: Node
    private lateinit var currentState: State
    private lateinit var possibleMoves: List<Move>

    fun run(): Move {
        val rootNode = Node(player = rootState.playersTurn)

        for (i in 0 until limit) {
            currentNode = rootNode
            possibleMoves = currentState.getAllPossibleMoves()
        }

        return TODO()
    }

}
package ai

import Move
import player.Player
import kotlin.math.ln
import kotlin.math.sqrt

data class Node(
    val parent: Node? = null,
    val children: MutableList<Node> = mutableListOf(),
    val player: Player,
    val moveToPlay: Move? = null,
    var totalScore: Long = 0,
    var visits: Int = 0,
    var considerations: Int = 1,
) {

    fun getUntriedMoves(possibleMoves: List<Move>): List<Move> {
        val triedMoves = children.mapNotNull { it.moveToPlay }

        if (triedMoves.isNotEmpty()) {
            return possibleMoves - triedMoves
        }
        return possibleMoves
    }

    fun selectChild(possibleMoves: List<Move>, explorationFactor: Double): Node? {
        var selection: Node? = children.firstOrNull { possibleMoves.contains(it.moveToPlay) }
        var selectionScore = -1.0

        for (child in children) {
            if (possibleMoves.contains(child.moveToPlay)) {
                val currentScore = calculateUCTScore(child, explorationFactor)
                if (currentScore > selectionScore) {
                    selection = child
                    selectionScore = currentScore
                }
                child.considerations++
            }
        }

        return selection
    }

    private fun calculateUCTScore(node: Node, explorationFactor: Double): Double {
        return (node.totalScore / node.visits) + (explorationFactor * sqrt(ln(node.considerations.toDouble()) / node.visits))
    }

    fun addChild(move: Move, player: Player): Node {
        val newNode = Node(parent = this, player = player, moveToPlay = move)
        children.add(newNode)
        return newNode
    }

    fun update(winner: Player, score: Long) {
        visits++
        totalScore += if (winner.name() == player.name()) score else 1 - score
    }

    override fun toString(): String {
        return "totalScore: $totalScore, moveToPlay: $moveToPlay, " +
                "children(size): ${children.size}, player: $player, visits: $visits, " +
                "considerations: $considerations"
    }
}
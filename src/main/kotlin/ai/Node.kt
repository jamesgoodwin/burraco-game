package ai

import Move
import player.Player

data class Node(val parent : Node? = null,
                val children: MutableList<Node>? = mutableListOf(),
                val player: Player,
                val moveToPlay: Move? = null,
                var totalScore: Int = 0,
                var visits: Int = 0,
                var considerations: Int = 1)
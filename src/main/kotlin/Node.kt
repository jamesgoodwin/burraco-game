import player.Player

data class Node(val parent : Node?,
                val children: MutableList<Node>,
                val player: Player,
                val moveToPlay: Move,
                var totalScore: Int = 0,
                var visits: Int = 0,
                var considerations: Int = 1)
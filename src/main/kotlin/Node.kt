data class Node(val parent : Node?,
                val children: MutableList<Node>,
                val player: Player,
                val moveToPlay: Move,
                var totalScore: Int,
                var visits: Int,
                var considerations: Int)
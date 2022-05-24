package solver

import core.*

class HumanV2 : AbstractPlayer() {
    override fun getNextMove(): Move {
        println("Enter w (UP), d (EAST), s (SOUTH), a (WEST) or x (WAIT)")
        val answer = readLine()
        val move = when (answer) {
            "w" -> WalkMove(Direction.NORTH)
            "d" -> WalkMove(Direction.EAST)
            "s" -> WalkMove(Direction.SOUTH)
            "a" -> WalkMove(Direction.WEST)
            else -> WaitMove
        }
        return move
    }

    override fun setMoveResult(result: MoveResult) {
        println(result.status)
    }
}

enum class Strategy {
    EXPLORE_SPAWN_AREA,
    CHECK_TO_MERGE_AREA,
    CYCLE_PREVENTION
}

enum class Decision {
    EXPLORE_START,
    EXPLORE_WALL,


}
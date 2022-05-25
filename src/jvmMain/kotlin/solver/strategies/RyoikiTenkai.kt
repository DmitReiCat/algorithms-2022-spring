package solver.strategies

import core.Direction
import core.Move
import core.WalkMove
import solver.PlayerMap

class RyoikiTenkai(val currentMap: PlayerMap) : Strategy {

    init {
        currentMap.calcToDiscover()
    }

    var radius = 0
    var lastDirection: Direction? = null
    var nextDirection = Direction.SOUTH
    val nextMoves = ArrayDeque<Move>()

    // TODO("randomized start dirs")
    private fun addNextMoves() {
        nextMoves.add(WalkMove(Direction.SOUTH))
        for (i in 1..radius) {
            nextMoves.add(WalkMove(Direction.EAST))
        }
        val directionSet = setOf(Direction.NORTH, Direction.WEST, Direction.SOUTH)
        for (direction in directionSet) {
            for (j in 1..radius * 2) {
                nextMoves.add(WalkMove(direction))
            }
        }
        for (i in 1..radius) {
            nextMoves.add(WalkMove(Direction.EAST))
        }
    }

    override fun nextMove(): Move =
        nextMoves.removeFirstOrNull()?.also { currentMap.lastMove = (it as WalkMove).direction } ?: let {
            radius++
            addNextMoves()
            currentMap.lastMove = (nextMoves.first() as WalkMove).direction
            nextMoves.removeFirst()
        }




}


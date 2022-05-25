package solver.strategies

import core.Direction
import core.Move
import core.WalkMove
import solver.PlayerMap

//class DomainExpansion(val playerMaps: MutableList<PlayerMap>, var mapIndex: Int, onIndexUpdate: (Int) -> Unit) : GlobalStrategy {
//
//    var currentStrategy: Strategy? = null
//
//
//    override fun onMapIndexChange(index: Int) {
//        onIndexUpdate(mapIndex)
//    }
//
//    override fun isActive(): Boolean {
//
//    }
//
//
//    override fun nextMove(): Move = currentStrategy?.nextMove() ?: let {
//        playerMaps.add(PlayerMap(true, startLocation))
////            println("START LOCATION IS $startLocation")
//        ViewModel.updatePlayerMap(
//            index = playerMaps.lastIndex,
//            current = Pair(startLocation, Entrance),
//            relStartLoc = startLocation
//        )
//        currentStrategy = SpiralDiscovery(playerMaps[0])
//        currentStrategy!!.nextMove()
//
//
//}

class SpyralExpansion(val currentMap: PlayerMap): Strategy {
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

class WallExploration(val currentMap: PlayerMap, ): Strategy {
    init {
        currentMap.calcToDiscover()
    }
    val wallDirection = currentMap.lastMove

    var lastDirection: Direction? = null
    var nextDirection = Direction.SOUTH
    val nextMoves = ArrayDeque<Move>()
    var isFirstMove = true

    private fun addNextMoves() {
        if (!isFirstMove) nextMoves.add(WalkMove(wallDirection))
        nextMoves.add(WalkMove(wallDirection.turnLeft()))
        isFirstMove = false
    }

    override fun nextMove(): Move =
        nextMoves.removeFirstOrNull()?.also { currentMap.lastMove = (it as WalkMove).direction } ?: let {
            addNextMoves()
            currentMap.lastMove = (nextMoves.first() as WalkMove).direction
            nextMoves.removeFirst()
        }
}




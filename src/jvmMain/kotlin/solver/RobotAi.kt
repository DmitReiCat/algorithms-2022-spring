package solver

import core.*
import graphics.ViewModel
import solver.strategies.RyoikiTenkai
import solver.strategies.Strategy

class HumanV2 : AbstractPlayer() {

    var playerMaps = mutableListOf<PlayerMap>()
    var currentMapIndex = 0
    var treasureFound = false

    var currentStrategy: Strategy? = null


    override fun getNextMove(): Move =
        currentStrategy?.nextMove() ?: let {
            playerMaps.add(PlayerMap(true, startLocation))
//            println("START LOCATION IS $startLocation")
            ViewModel.updatePlayerMap(
                index = playerMaps.lastIndex,
                current = Pair(startLocation, Entrance),
                relStartLoc = startLocation
            )
            currentStrategy = RyoikiTenkai(playerMaps[0])
            currentStrategy!!.nextMove()
        }
//        println("Enter w (UP), d (EAST), s (SOUTH), a (WEST) or x (WAIT)")
//        val answer = readLine()
//        val move = when (answer) {
//            "w" -> WalkMove(Direction.NORTH)
//            "d" -> WalkMove(Direction.EAST)
//            "s" -> WalkMove(Direction.SOUTH)
//            "a" -> WalkMove(Direction.WEST)
//            else -> WaitMove
//        }
//        return move
//    }

    private fun addNewMap() {
        currentMapIndex++
        playerMaps.add(PlayerMap(false, Location(0,0)))
        ViewModel.updatePlayerMap(
            index = playerMaps.lastIndex,
            current = Pair(Location(0,0), Wormhole(1)),
            relStartLoc = Location(0,0)
        )
        currentStrategy = RyoikiTenkai(playerMaps.last())
    }


    override fun setMoveResult(result: MoveResult) {
        val resInMap = playerMaps[currentMapIndex]
        resInMap.setLastMoveResults(result)
        ViewModel.updatePlayerMap(
            currentMapIndex,
            resInMap.discoverCellsToVisualize,
            resInMap.roomToVisualize,
            playerMaps[currentMapIndex].relativeSpawn
        )
        playerMaps[currentMapIndex].apply {
            when(result.room) {
                Entrance -> entrance = currentLocation
                Exit -> exit = currentLocation
                is WithContent -> { treasureFound = true; treasure = currentLocation }
                is Wormhole -> { wormholes[currentLocation] = WormholeId(); addNewMap() } //TODO("WORMHOLE without id")
                else -> {  } //do nothing
            }
        }
        when(result.room) {
            Entrance -> playerMaps[currentMapIndex].apply { entrance = currentLocation }
            Exit -> playerMaps[currentMapIndex].apply { exit = currentLocation }
            is WithContent -> playerMaps[currentMapIndex].apply { treasureFound = true; treasure = currentLocation }
            is Wormhole -> playerMaps[currentMapIndex].apply { wormholes[currentLocation] = WormholeId() }
            else -> {  } //do nothing
        }
        resInMap.discoverCellsToVisualize = mutableSetOf()
//        println(result.status)
    }
}

//enum class Strategy {
//    EXPLORE_SPAWN_AREA,
//    CHECK_TO_MERGE_AREA,
//    CYCLE_PREVENTION
//}

enum class Decision {
    EXPLORE_START,
    EXPLORE_WALL,


}
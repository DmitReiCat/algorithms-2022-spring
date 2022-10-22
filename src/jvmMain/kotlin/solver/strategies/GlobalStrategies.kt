package solver.strategies

import core.*
import graphics.ViewModel
import solver.PlayerMap
import solver.PlayerMapFactory
import solver.minus
import solver.plus


class FindAllWormholes(private val mapFactory: PlayerMapFactory) : AbstractGlobalStrategy(mapFactory) {

    init {
        subStrategy = RyoikiTenkai(mapFactory.currentMap)
    }

    private var checkRouteToStart: ArrayDeque<WalkMove>? = null

    private var checkRouteFromStart: ArrayDeque<WalkMove> = ArrayDeque()

    override fun getNextMove(): WalkMove =
        subStrategy.nextMove() ?: let {
            when (subStrategy) {
                is CheckLocation -> let {
                    subStrategy =
                        CheckLocation(findClosestOrGoal(mapFactory.currentMap, mapFactory.currentMap.spawnLocation))
                }
                is RyoikiTenkai -> subStrategy.apply {
                    nextMoves = findClosestOrGoal(
                        mapFactory.currentMap,
                        mapFactory.currentMap.exit
                    ) // if all locations in map are discovered
                }
            }
            subStrategy.nextMove()!!
        }

    private fun PlayerMap.recalculateLocationsAndMerge(room: Room) {
        println("maps are merging!, total maps size = ${mapFactory.playerMaps.size}")
        when (room) {
            Entrance -> {
                val mapToMerge = this
                val startMap = mapFactory.playerMaps[0]
                val offset = mapFactory.lastDirection + mapToMerge.currentLocation - mapFactory.actualStartLocation //TODO(Right calculations)
                startMap.knownLocations += mapToMerge.knownLocations.mapKeys { it.key - offset }
                startMap.toDiscover += mapToMerge.toDiscover.map { it - offset }.filter { it - offset !in knownLocations }

                if (startMap.treasure == null) startMap.treasure = mapToMerge.treasure?.minus(offset)
                startMap.wormholes += mapToMerge.wormholes.mapKeys { it.key - offset }
                startMap.currentLocation = mapToMerge.currentLocation - offset
                mapFactory.currentMapIndex = 0
                mapFactory.playerMaps.remove(this)
                mapFactory.updateGraphics()
                subStrategy = RyoikiTenkai(mapFactory.currentMap)
            }
            else -> {}
        }
    }


    override fun setResults(result: MoveResult) {
        if (result.successful) {
            when (result.room) {
                Entrance -> if (mapFactory.currentMapIndex != 0) {
                    mapFactory.currentMap.recalculateLocationsAndMerge(result.room)
                    when (subStrategy) {
                        is CheckLocation -> subStrategy = RyoikiTenkai(mapFactory.currentMap)
                        is RyoikiTenkai -> {} // DO nothing
                    }
                }
                Exit -> {}
                is WithContent -> {}
                is Wormhole -> {
                    if (checkRouteToStart == null) {
                        checkRouteToStart = findClosestOrGoal(
                            currentMap = mapFactory.currentMap,
                            goal = mapFactory.currentMap.entrance,
                            start = mapFactory.lastDirection + mapFactory.currentMap.currentLocation
                        )
                        checkRouteFromStart = ArrayDeque(checkRouteToStart!!.reversed())
                    }
                    subStrategy = CheckLocation(checkRouteToStart!!)
                }
                else -> {}
            }
        } else {
            println("starting calculations of goal")
            when (subStrategy) {
                is CheckLocation -> subStrategy =
                    CheckLocation(findClosestOrGoal(mapFactory.currentMap, mapFactory.currentMap.spawnLocation))

                is RyoikiTenkai -> {}
            }
        }
    }
}



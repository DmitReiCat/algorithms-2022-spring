package solver.strategies

import core.Location
import solver.PlayerMap
import solver.distance
import java.util.PriorityQueue


fun reconstructFromPath(cameFrom: Map<Location, Location>, current: Location) {

}


fun findAStarRouteCurrentMap(currentMap: PlayerMap, endLocation: Location) {
    val start = currentMap.currentLocation

    val openSet = PriorityQueue<Location>(compareBy<Location> { it.distance(endLocation) } )
    openSet.add(currentMap.currentLocation)
    val cameFrom = mutableMapOf<Location, Location>()

    val gScore = mutableMapOf<Location, Int>().withDefault { Int.MAX_VALUE }
    gScore[start] = 0

    val fScore = mutableMapOf<Location, Int>().withDefault { Int.MAX_VALUE }
    fScore[start] = start.distance(endLocation)

    while (openSet.isNotEmpty()) {
        val current = openSet.peek()
        if (current == endLocation) return reconstructFromPath(cameFrom, current)
        openSet.remove()

    }



}
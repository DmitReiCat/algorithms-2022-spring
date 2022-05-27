package solver.strategies

import core.*
import solver.*


fun findClosestOrGoal(
    currentMap: PlayerMap,
    goal: Location? = null,
    start: Location = currentMap.currentLocation
): ArrayDeque<WalkMove> {

    val toExpand = ArrayDeque<Location>()
    val knownLocations = mutableMapOf(start to 0)
    var isRouteFound = false
    lateinit var endLocation: Location

    fun expandUntilGoal(location: Location) {
        for (direction in directionSet) {
            val possibleLocation = direction + location
            if (possibleLocation.let {
                    it !in knownLocations && it !in toExpand
                            && (it in currentMap.knownLocations && currentMap.knownLocations[it] !is Wall
                            || (it in currentMap.toDiscover && goal == null)) // todo(Check for Maze)
                }
            ) {
                toExpand.addLast(possibleLocation)
            }
        }
        val newLocation = toExpand.removeFirst()
        var minSteps = Int.MAX_VALUE
        for (direction in directionSet) {
            minSteps = minOf((knownLocations[direction + newLocation] ?: Int.MAX_VALUE), minSteps)
        }
        knownLocations[newLocation] = minSteps + 1
        println("$newLocation ${newLocation in currentMap.toDiscover} ${newLocation in currentMap.toDiscover && goal == null}")
        if (newLocation in currentMap.toDiscover && goal == null || newLocation == goal) {
            isRouteFound = true
            endLocation = newLocation
        } else {
            expandUntilGoal(newLocation)
        }
    }
    expandUntilGoal(start)
    println("   goal= $endLocation")
    println("   currentLoc= ${currentMap.currentLocation}")
    println("   currentLoc= ${start}")
    val route = ArrayDeque<WalkMove>()
    reconstructFromMap(knownLocations, start, endLocation, route)
    println(route.map { it.direction })
    return route

}


fun reconstructFromMap(
    locations: Map<Location, Int>,
    start: Location,
    end: Location,
    currentRoute: ArrayDeque<WalkMove>
) {
    var minStep = Int.MAX_VALUE
    lateinit var resDirection: Direction
    lateinit var resEndLocation: Location
    for (direction in directionSet) {
        val location = direction + end
        if ((locations[location] ?: Int.MAX_VALUE) <= minStep) {
            minStep = locations[location] ?: Int.MAX_VALUE
            resDirection = direction
            resEndLocation = direction + end
        }
    }
    currentRoute.addLast(WalkMove(resDirection.turnBack()))
    if (start != resEndLocation) reconstructFromMap(locations, start, resEndLocation, currentRoute)
}


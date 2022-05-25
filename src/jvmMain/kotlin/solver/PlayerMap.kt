package solver

import core.*
import core.Exit
import core.Wormhole
import kotlin.math.abs


class PlayerMap(val isPrimary: Boolean, val relativeSpawn: Location) {

    var discoverCellsToVisualize = mutableSetOf<Location>()
    lateinit var roomToVisualize: Pair<Location, Room>

    var currentLocation = relativeSpawn
    val knownLocations = mutableMapOf<Location, Room>(relativeSpawn to Empty)
    val wormholes = mutableMapOf<Location, WormholeId>()
    val toDiscover = mutableSetOf<Location>()
    var entrance: Location? = null
    var exit: Location? = null

    var treasure: Location? = null
    lateinit var lastMove: Direction

    init {
        calcToDiscover()
    }

    fun calcToDiscover() {
        for (x in -1..1) {
            for (y in -1..1) {
                val location = Location(currentLocation.x + x, currentLocation.y + y)
                if (abs(x) != abs(y) && knownLocations[location] == null && location !in toDiscover) {
                    toDiscover.add(location)
                    discoverCellsToVisualize.add(location)
//                    println("$location; $discoverCellsToVisualize; ${knownLocations[location]}; $knownLocations")
                }
            }
        }
    }
    fun setLastMoveResults(res: MoveResult) {
        roomToVisualize = (lastMove + currentLocation to res.room)
        if (res.successful) {
            currentLocation = lastMove + currentLocation
        }
        knownLocations[currentLocation] = res.room
        if (currentLocation in toDiscover) toDiscover.remove(currentLocation)
        if (currentLocation in discoverCellsToVisualize) discoverCellsToVisualize.remove(currentLocation)
        calcToDiscover()



        when(res.room) {
            Entrance -> entrance = currentLocation
            Exit -> exit = currentLocation
            is WithContent -> treasure = currentLocation
            is Wormhole -> wormholes[currentLocation] = WormholeId()
            else -> {}
        }


    }


}
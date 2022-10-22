package solver

import core.*
import core.Exit
import core.Wormhole


class PlayerMap(val spawnLocation: Location, isPrimary: Boolean) {
    var currentLocation = spawnLocation
    val knownLocations = mutableMapOf<Location, Room>()
    val wormholes = mutableMapOf<Location, Wormhole>()
    var toDiscover = mutableSetOf<Location>()
    var entrance: Location? = null
    var exit: Location? = null

    var treasure: Location? = null


    init {
        if (isPrimary) {
            knownLocations[spawnLocation] = Entrance
            entrance = spawnLocation
        } else {
            knownLocations[spawnLocation] = Wormhole(-1) // todo(Add actual wormhole id)
            wormholes[spawnLocation] = Wormhole(-1) // todo(Add actual wormhole id)
        }
        calcToDiscover()
    }


    fun calcToDiscover() {
        for (direction in directionSet) {
            val location = direction + currentLocation
            if (location !in toDiscover && knownLocations[location] == null) {
                toDiscover.add(location)
            }
        }
    }

    // todo(revisit for changes + toDiscover)
    fun setLastMoveResults(res: MoveResult, lastMove:Direction) {
        if (lastMove + currentLocation in toDiscover) toDiscover.remove(lastMove + currentLocation)
        if (res.successful) {
            currentLocation = lastMove + currentLocation
            knownLocations[currentLocation] = res.room
        } else {
            knownLocations[lastMove + currentLocation] = res.room
        }
        if (currentLocation in toDiscover) toDiscover.remove(currentLocation)
        calcToDiscover()

        when(res.room) {
            Entrance -> entrance = currentLocation
            Exit -> exit = currentLocation
            is WithContent -> treasure = currentLocation
            is Wormhole -> wormholes[currentLocation] = Wormhole(0)
            else -> {}
        }
    }
}


package graphics

import solver.HumanAI
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import core.*
import lab.Controller
import lab.Labyrinth
import samples.Human
import solver.minus
import solver.plus
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.math.abs


// TODO(Make a game object on press start)
object ViewModel {
    var gameState = GameState()
    val isRunning = mutableStateOf(false)
    val mapsTotal = mutableStateOf(0)

    fun updateFullMap(
        knownLocations: MutableMap<Location, Room>,
        toDiscover: MutableSet<Location>
    ) {


        var maxX: Int = Int.MIN_VALUE
        var minX: Int = Int.MAX_VALUE
        var maxY: Int = Int.MIN_VALUE
        var minY: Int = Int.MAX_VALUE


        toDiscover.forEach { location ->
            if (location.x < minX) minX = location.x
            if (location.x > maxX) maxX = location.x
            if (location.y < minY) minY = location.y
            if (location.y > maxY) maxY = location.y
        }

        knownLocations.forEach { (location, _) ->
            if (location.x < minX) minX = location.x
            if (location.x > maxX) maxX = location.x
            if (location.y < minY) minY = location.y
            if (location.y > maxY) maxY = location.y
        }

        val mapOffset = Location(minX, minY)

        var mapHeight = maxY - minY + 1
        var mapWight = maxX - minX + 1

        val newUIMap = Array(mapHeight) { Array(mapWight) { "" } }
        for (y in 0 until mapHeight) {
            for (x in 0 until mapWight) {
                val ch = knownLocations[Location(x, y) + mapOffset]?.let {
                    getCharFromRoom(it).toString()
                } ?:  ""
                newUIMap[y][x] = ch
            }
        }
        for (y in 0 until mapHeight) {
            for (x in 0 until mapWight) {
                val ch = if (toDiscover.contains(Location(x, y) + mapOffset)) "*" else ""
                if (ch == "*") newUIMap[y][x] = ch
            }
        }

        gameState.apply {
            if (mapsTotal.value == 1) {
                allMaps.add(mutableStateOf(newUIMap))
                mapsTotal.value++
            }
            allMaps[1].value = newUIMap
        }
    }


    fun updateCurrentLocation(location: Location) {
        gameState.apply {
            if (currPos == null) startLoc = location
            currPos = location
            stateOfCurrPose.value = location
            prevPos = currPos
        }
    }

    fun getCharFromRoom(room: Room) = when (room) {
        is Empty -> ' '
        is Wall -> '#'
        is Wormhole -> 'O'
        is Entrance -> 'S'
        is Exit -> 'E'
        is WithContent -> if (room.content != null) 'T' else ' '
        else -> '?'
    }

    fun initLabyrinth(lab: Labyrinth) {
        val map = Array(lab.height + 2) { Array(lab.width + 2) { "" } }
        for (y in -1..lab.height) {
            for (x in -1..lab.width) {
                val ch = getCharFromRoom(lab[x, y])
                if (ch == 'S') updateCurrentLocation(Location(x, y))
                map[y + 1][x + 1] = ch.toString()
            }
        }
        gameState.apply {
            allMaps.add(mutableStateOf(map))
            allMapsOffsets.add(Location(0, 0))
            labHeight = lab.height + 2
            labWidth = lab.width + 2
            isRunning.value = true
            mapsTotal.value = 1
        }
    }


    fun start() {
        gameState = GameState()
        gameState.apply {
            if (!isRunning.value) {
                thread {
                    val playerRun = object : AbstractPlayerRun() {
                        override fun createPlayer() = HumanAI()
                    }
                    playerRun.doTestLab(pathToLabyrinth)
                    isRunning.value = false
                }
            }
        }
    }
}

abstract class AbstractPlayerRun {

    abstract fun createPlayer(): Player

    fun doTestLab(fileName: String) {
        val lab = Labyrinth.createFromFile(fileName)
        val player = createPlayer()
        val controller = Controller(lab, player)
        sleep(100) // wait for compose to initialize view
        val actualResult = controller.makeMoves(500)
        if (actualResult.exitReached) {
            println("You won!")
        } else {
            println("You lose!")
        }
    }
}

class GameState {
    var currPos: Location? = null
    var prevPos: Location? = null
    val stateOfCurrPose = mutableStateOf(currPos)
    var labHeight = 0
    var labWidth = 0
    lateinit var startLoc: Location
    var allMaps = mutableListOf<MutableState<Array<Array<String>>>>()
    val allMapsOffsets = mutableListOf<Location?>()

}

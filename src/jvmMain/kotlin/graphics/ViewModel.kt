package graphics

import solver.HumanV2
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import core.*
import lab.Controller
import lab.Labyrinth
import samples.BrainDead
import samples.Human
import solver.minus
import solver.plus
import java.lang.Thread.sleep
import java.util.Locale
import kotlin.concurrent.thread


// TODO(Make a game object on press start)
object ViewModel {
    var currPos: Location? = null
    var prevPos: Location? = null
    val stateOfCurrPose = mutableStateOf(currPos)
    var labHeight = 0
    var labWidth = 0
    lateinit var startLoc: Location

//    var lastWormhPos: Location? = null
//    var currWormhPos: Location? = null

    var allMaps = mutableListOf<Array<Array<MutableState<String>>>>()
    val allMapsOffsets = mutableListOf<Location?>()
//    var currentMap: Array<Array<MutableState<String>>>? = null

    val mapsTotal = mutableStateOf(0)
    val isRunning = mutableStateOf(false)


    fun updatePassedLocation(mapIndex: Int) {
        if (prevPos != null && allMaps[mapIndex][prevPos!!.y + 1][prevPos!!.x + 1].value == " ") {
            allMaps[mapIndex][prevPos!!.y + 1][prevPos!!.x + 1].value = "*"
        }
    }

    fun updatePlayerMap(
        index: Int,
        toDiscover: Set<Location> = setOf<Location>(),
        current: Pair<Location, Room>,
        relStartLoc: Location
    ) {
//        val offset = if (index == 0) Location(1,1) else prevPos!! - relStartLoc + Location(1,1)

        val ch = getCharFromRoom(current.second)
        if (index + 1 > allMaps.size - 1) {
            addEmptyMap()
//            sleep(1000) // TODO("Remove sleep parameter")
        } else {

        }
        val offset = if (index == 0) Location(1,1) else allMapsOffsets[index + 1]!!
        allMaps[index + 1][(current.first + offset).y][(current.first + offset).x].value = ch.toString()
        println("index =${index + 1} offset=$offset")
        println("######")
        toDiscover.forEach {
            allMaps[index + 1][it.y + offset.y][it.x + offset.x].value = "*"
        }
//        println("########")

//        allMaps[index + 1] = when(current.second) {
//            Empty -> allMaps[index + 1][]
//            Entrance -> TODO()
//            Exit -> TODO()
//            Wall -> TODO()
//            is WithContent -> TODO()
//            is Wormhole -> TODO()
//        }
    }

    fun updateCurrentLocation(location: Location) {
        if (currPos == null) startLoc = location
        currPos = location
        stateOfCurrPose.value = location
        prevPos = currPos
    }
//    fun updatePlayerMaps(mapIndex: Int, location: Location) {
//        _currentPlayerMap[mapIndex]
//    }

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
        val map = Array(lab.height + 2) { Array(lab.width + 2) { mutableStateOf("") } }
        for (y in -1..lab.height) {
            for (x in -1..lab.width) {
                val ch = getCharFromRoom(lab[x, y])
                if (ch == 'S') updateCurrentLocation(Location(x,y))
                map[y + 1][x + 1].value = ch.toString()
            }
        }
        allMaps.add(map)
        allMapsOffsets.add(Location(0,0))
        labHeight = lab.height + 2
        labWidth = lab.width + 2
//        currentMap = map
        isRunning.value = true
        mapsTotal.value = 1
    }

    fun addEmptyMap() {
        val map = Array(labHeight) { Array(labWidth) { mutableStateOf("") } }
        allMaps.add(map)
        allMapsOffsets.add(currPos!! + Location(1,1))
        mapsTotal.value++
    }
    fun addMapWithDefaults() {
        TODO("To prevent sleep")
    }


    fun start() {
        if (!isRunning.value) {
            thread {
                val playerRun = object : AbstractPlayerRun() {
                    override fun createPlayer() = Human()
                }
                playerRun.doTestLab(pathToLabyrinth)
                isRunning.value = false

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
        val actualResult = controller.makeMoves(500)
        if (actualResult.exitReached) {
            println("You won!")
        }
        else {
            println("You lose!")
        }
//        assertEquals(controller.playerPath.toString(), expectedResult.exitReached, actualResult.exitReached)
//        if (expectedResult.exitReached && actualResult.exitReached && expectedResult.moves >= 0) {
//            assertEquals(controller.playerPath.toString(), expectedResult.moves, actualResult.moves)
//        }
    }
}

class BrainDeadRun : AbstractPlayerRun() {
    override fun createPlayer() = BrainDead()
}

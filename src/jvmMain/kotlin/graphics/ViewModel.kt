package graphics

import solver.HumanV2
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import core.*
import lab.Controller
import lab.Labyrinth
import samples.BrainDead
import kotlin.concurrent.thread


// TODO(Make a game object on press start)
object ViewModel {
    var currPos: Location? = null
    var prevPos: Location? = null
    val stateOfCurrPose = mutableStateOf(currPos)
    var labHeight = 0
    var labWidth = 0

//    var lastWormhPos: Location? = null
//    var currWormhPos: Location? = null

    var allMaps = mutableListOf<Array<Array<MutableState<String>>>>()
//    var currentMap: Array<Array<MutableState<String>>>? = null

    val mapsTotal = mutableStateOf(0)
    val isRunning = mutableStateOf(false)


    fun updatePassedLocation(mapIndex: Int) {
        if (prevPos != null && allMaps[mapIndex][prevPos!!.y + 1][prevPos!!.x + 1].value == " ") {
            allMaps[mapIndex][prevPos!!.y + 1][prevPos!!.x + 1].value = "*"
        }
    }

    fun updateCurrentLocation(location: Location) {
        currPos = location
        stateOfCurrPose.value = location
        prevPos = currPos
    }
//    fun updatePlayerMaps(mapIndex: Int, location: Location) {
//        _currentPlayerMap[mapIndex]
//    }

    fun initLabyrinth(lab: Labyrinth) {
        val map = Array(lab.height + 2) { Array(lab.width + 2) { mutableStateOf("") } }
        for (y in -1..lab.height) {
            for (x in -1..lab.width) {
                val ch = when (lab[x, y]) {
                    is Empty -> ' '
                    is Wall -> '#'
                    is Wormhole -> 'O'
                    is Entrance -> 'S'
                    is Exit -> 'E'
                    is WithContent -> if (lab[x, y].content != null) 'T' else ' '
                    else -> '?'
                }
                map[y + 1][x + 1].value = ch.toString()
            }
        }
        allMaps.add(map)
        labHeight = lab.height + 2
        labWidth = lab.width + 2
//        currentMap = map
        isRunning.value = true
        mapsTotal.value = 1
    }

    fun addMap() {
        val map = Array(labHeight) { Array(labWidth) { mutableStateOf("") } }
        allMaps.add(map)
        mapsTotal.value++
    }


    fun start() {
        if (!isRunning.value) {
            thread {
                val playerRun = object : AbstractPlayerRun() {
                    override fun createPlayer() = HumanV2()
                }
                playerRun.doTestLab("labyrinths/lab6.txt", Controller.GameResult(100, exitReached = false))
                isRunning.value = false

            }
        }
    }



}


abstract class AbstractPlayerRun {

    abstract fun createPlayer(): Player

    fun doTestLab(fileName: String, expectedResult: Controller.GameResult) {
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

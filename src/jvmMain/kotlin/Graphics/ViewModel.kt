package Graphics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import core.*
import lab.Controller
import lab.Labyrinth
import samples.BrainDead
import kotlin.concurrent.thread

object ViewModel {
    const val FRAMERATE = 1000L

    var currPos: Location? = null
    var prevPos: Location? = null
    val stateOfCurrPose = mutableStateOf(currPos)

    var lastWormhPos: Location? = null
    var currWormhPos: Location? = null

    var _currentMap: Array<Array<MutableState<String>>>? = null

    val isMapInited = mutableStateOf(false)

    var currentMap = mutableStateOf(_currentMap?.map { it.toList() }?.toList())

    fun updateMap(location: Location) {
        if (prevPos != null && _currentMap!![prevPos!!.y + 1][prevPos!!.x + 1].value == " ") {
            _currentMap!![prevPos!!.y + 1][prevPos!!.x + 1].value = "*"
        }
        currPos = location
        stateOfCurrPose.value = location
        prevPos = currPos
    }

    fun initMap(lab: Labyrinth) {
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
        _currentMap = map
        isMapInited.value = !isMapInited.value
    }


    fun start() {
        if (!isMapInited.value) {
            thread {
                val playerRun = object : AbstractPlayerRun() {
                    override fun createPlayer() = BrainDead()
                }
                playerRun.doTestLab("labyrinths/lab6.txt", Controller.GameResult(100, exitReached = false))
                isMapInited.value = false
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
//        assertEquals(controller.playerPath.toString(), expectedResult.exitReached, actualResult.exitReached)
//        if (expectedResult.exitReached && actualResult.exitReached && expectedResult.moves >= 0) {
//            assertEquals(controller.playerPath.toString(), expectedResult.moves, actualResult.moves)
//        }
    }
}

class BrainDeadRun : AbstractPlayerRun() {
    override fun createPlayer() = BrainDead()
}

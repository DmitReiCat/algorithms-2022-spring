package samples

import core.Player
import lab.Controller
import lab.Labyrinth
import kotlin.test.DefaultAsserter.assertEquals

abstract class AbstractPlayerTest {

    abstract fun createPlayer(): Player

    fun doTestLab(fileName: String, expectedResult: Controller.GameResult) {
        val lab = Labyrinth.createFromFile(fileName)
        val player = createPlayer()
        val controller = Controller(lab, player)
        val actualResult = controller.makeMoves(500)
        assertEquals(controller.playerPath.toString(), expectedResult.exitReached, actualResult.exitReached)
        if (expectedResult.exitReached && actualResult.exitReached && expectedResult.moves >= 0) {
            assertEquals(controller.playerPath.toString(), expectedResult.moves, actualResult.moves)
        }
    }

}
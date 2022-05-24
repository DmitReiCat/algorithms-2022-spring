package samples

import lab.Controller
import org.junit.Test

class AlwaysNorthTest : AbstractPlayerTest() {

    override fun createPlayer() = AlwaysNorth()

    @Test
    fun testLab1() {
        doTestLab("labyrinths/lab1.txt", Controller.GameResult(100, exitReached = false))
    }

}
package lab

import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class LabyrinthTest {
    @Test
    fun testLabyrinths() {
        val dir = File("labyrinths")
        assertTrue(dir.isDirectory)
        for (file in dir.listFiles()) {
            val lab = Labyrinth.createFromFile(file)
            assertTrue(lab.isValid())
        }
    }
}
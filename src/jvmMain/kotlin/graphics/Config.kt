package graphics

import androidx.compose.ui.unit.dp

val CELL_SIZE = 30.dp
//var framesPerSecond = 1.0
var framesPerSecond = 0.3
val FRAMERATE get() = (1000.0 / framesPerSecond).toLong()
const val pathToLabyrinth = "labyrinths/emptyWithExit"
//const val pathToLabyrinth = "labyrinths/lab6.txt"
//const val pathToLabyrinth = "labyrinths/maze.txt"
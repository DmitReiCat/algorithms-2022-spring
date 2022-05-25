package graphics

import androidx.compose.ui.unit.dp

val CELL_SIZE = 30.dp
val framesPerSecond = 10
val FRAMERATE get() = (1000.0 / framesPerSecond).toLong()
//const val pathToLabyrinth = "labyrinths/emptyWithExit"
const val pathToLabyrinth = "labyrinths/lab6.txt"
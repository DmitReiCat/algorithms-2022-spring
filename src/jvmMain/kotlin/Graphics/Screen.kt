package Graphics

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val CELL_SIZE = 50.dp

object Colors {
    val WALL = Color(0xFF000000)
    val PASSED = Color(0xFFB8860B)
    val TREASURE = Color(0xFFB8860B)
    val START = Color(0xFFFF0000)
    val EXIT = Color(0xFF00FF00)
    val TO_PASS = Color(0xFFFFFF00)
    val CURRENT = Color(0xFFF0E68C)
    val WORMHOLE = Color(0xFF8A2BE2)
    val LAST_WORMHOLE = Color(0xFF4B0082)
}


@Composable
@Preview
fun AppScreen() {
    val isMapInited by remember { ViewModel.isMapInited }
    
    MaterialTheme {
        Column {
            Button(
                enabled = !isMapInited,
                onClick = {
                    ViewModel.start()
                }
            ) {
                Text(
                    text = if (isMapInited) "Running..." else "Start"
                )
            }
            if (isMapInited) {
                for (y in ViewModel._currentMap!!.indices) {
                    Row {
                        for (x in ViewModel._currentMap!![y].indices) {
                            Cell(y, x)
                        }
                    }
                }
            }
        }
    }
}

//enum class Colors(color: Color) {
//  Start(Color(0xFFFF0000)),
//  End(Color(0x00FF00)),
//  Blue(Color(0x0000FF)),
//  Wall(Color(0xFF0000)),
//  Passed(Color(0xB8860B)),
//
//}

@Composable
fun Cell(y: Int, x: Int) {

    val currPos by remember { ViewModel.stateOfCurrPose }
    val ch by remember { ViewModel._currentMap!![y][x] }
    var color = when (ch) {
        "#" -> Colors.WALL
        "T" -> Colors.TREASURE
        "E" -> Colors.EXIT
        "S" -> Colors.START
        " " -> Color.White
        "*" -> Colors.PASSED
        else -> Colors.WORMHOLE
    }
    if (currPos?.x == x-1 && currPos?.y == y-1) color = Colors.CURRENT
//    println("${currPos} ${x-1} ${y-1})}")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(CELL_SIZE)
            .background(color)
    ) {
        if (ch !in setOf("#", "T", "*")) {
            Text(
                text = ch
            )
        }
    }
}
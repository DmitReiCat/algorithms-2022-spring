package graphics

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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


object Colors {
    val WALL = Color(0xFF000000)
    val PASSED = Color(0xFFFF8C00)
    val TREASURE = Color(0xFF006400)
    val START = Color(0xFFFF0000)
    val EXIT = Color(0xFF00FF00)
    val TO_PASS = Color(0xFFFFFF00)
    val CURRENT = Color(0xFFFF6347)
    val WORMHOLE = Color(0xFF8A2BE2)
    val LAST_WORMHOLE = Color(0xFF4B0082)

    val UNKNOWN = Color(0xFF088DA5)
}


@Composable
@Preview
fun AppScreen() {
    val isMapInited by remember { ViewModel.isRunning }
    val mapsTotal by remember { ViewModel.mapsTotal }

    MaterialTheme {
        LazyColumn {
            item {
                Column {
                    Row {
                        Button(
                            enabled = !isMapInited,
                            onClick = { ViewModel.start() }
                        ) {
                            Text(
                                text = if (isMapInited) "Running..." else "Start"
                            )
                        }
                        Button(
                            onClick = { framesPerSecond += 1 }
                        ) {
                            Text("FPS+")
                        }
                        Button(
                            onClick = { if (framesPerSecond > 1) framesPerSecond -= 1 else framesPerSecond /= 2.0 }
                        ) {
                            Text("FPS-")
                        }
                        Button(
                            onClick = { if (framesPerSecond > 10) framesPerSecond -= 10 else framesPerSecond = 1.0 }
                        ) {
                            Text("FPS--")
                        }
                        Button(
                            onClick = { framesPerSecond += 10 }
                        ) {
                            Text("FPS++")
                        }
                    }
                    if (mapsTotal != 0 && isMapInited) {

                            for (currentMapIndex in ViewModel.gameState.allMaps.indices) {
                                Text("Labyrinth $currentMapIndex")

                                for (y in ViewModel.gameState.allMaps[currentMapIndex].value.indices) {
                                    Row {
                                        for (x in ViewModel.gameState.allMaps[currentMapIndex].value[y].indices) {
                                            val ch = ViewModel.gameState.allMaps[currentMapIndex].value[y][x]
                                            Cell(currentMapIndex, y, x, ch)
                                        }
                                    }
                                }
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
fun Cell(currentMapIndex: Int, y: Int, x: Int, ch: String) {
    val currPos by remember { ViewModel.gameState.stateOfCurrPose }

    var color = when (ch) {
        "#" -> Colors.WALL
        "T" -> Colors.TREASURE
        "E" -> Colors.EXIT
        "S" -> Colors.START
        " " -> Color.White
        "*" -> Colors.PASSED
        "" -> Colors.UNKNOWN
        else -> Colors.WORMHOLE
    }
    if (currentMapIndex == 0 && currPos?.x == x-1 && currPos?.y == y-1) {
        color = Colors.CURRENT
    }
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(CELL_SIZE + 2.dp)
            .background(Color.Black)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(CELL_SIZE)
                .background(color)
        ) {
            if (ch !in setOf("#", "*")) {
                val text = if (ch == "") "?" else ch
                Text(
                    text = text
                )
            }
        }
    }

}
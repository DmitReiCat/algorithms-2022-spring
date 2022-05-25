package solver

import core.Location

data class WormholeId(val id: Int? = null, val next: Int? = null)

//enum class CellState {
//    EMPTY,
//    EXIT,
//    WORMHOLE,
//    SPAWN,
//}
//
//sealed class Cell
//object Empty: Cell()
//object Exit: Cell()
//class Wormhole(val tpLocation: Location): Cell()
//object ToDiscover:Cell()
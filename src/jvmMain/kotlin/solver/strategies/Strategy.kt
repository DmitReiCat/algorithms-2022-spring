package solver.strategies

import core.Move
import core.MoveResult
import solver.PlayerMap

interface Strategy {
    fun nextMove(): Move


}

interface GlobalStrategy: Strategy {
    fun onMapIndexChange(index: Int): Unit
    fun isActive(): Boolean

}

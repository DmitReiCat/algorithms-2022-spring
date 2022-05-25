package solver.strategies

import core.Move
import core.MoveResult

interface Strategy {
    fun nextMove(): Move

}
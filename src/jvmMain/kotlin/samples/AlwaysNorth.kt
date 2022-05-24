package samples

import core.AbstractPlayer
import core.Direction
import core.MoveResult
import core.WalkMove

class AlwaysNorth : AbstractPlayer() {
    override fun getNextMove() = WalkMove(Direction.NORTH)

    override fun setMoveResult(result: MoveResult) {}
}
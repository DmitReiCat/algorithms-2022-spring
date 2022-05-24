package samples

import core.AbstractPlayer
import core.Direction
import core.MoveResult
import core.WalkMove

open class BrainDead : AbstractPlayer() {

    protected var lastDirection = Direction.NORTH

    protected var lastSuccess = true

    protected open fun getDirection() =
            if (lastSuccess) lastDirection
            else lastDirection.turnRight()

    override fun getNextMove() = WalkMove(getDirection().apply { lastDirection = this })

    override fun setMoveResult(result: MoveResult) {
        lastSuccess = result.successful
    }
}
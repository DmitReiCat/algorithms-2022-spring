package samples

import samples.BrainDead

class BrainDeadLeft : BrainDead() {

    override fun getDirection() =
            if (lastSuccess) lastDirection
            else lastDirection.turnLeft()

}
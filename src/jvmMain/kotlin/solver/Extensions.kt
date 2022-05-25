package solver

import core.Location
import kotlin.math.sqrt


operator fun Location.plus(other: Location) = Location(this.x + other.x, this.y + other.y)
operator fun Location.minus(other: Location) = Location(this.x - other.x, this.y - other.y)
fun Location.distance(other: Location): Int =
    sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y).toDouble()).toInt()
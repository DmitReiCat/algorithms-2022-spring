package solver

import core.Location


operator fun Location.plus(other: Location) = Location(this.x + other.x, this.y + other.y)
operator fun Location.minus(other: Location) = Location(this.x - other.x, this.y - other.y)
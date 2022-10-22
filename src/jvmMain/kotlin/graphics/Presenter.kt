package graphics

import solver.PlayerMap

object Presenter {
    fun updateGraphics(currentMap: PlayerMap) {
        ViewModel.updateFullMap(
            currentMap.knownLocations,
            currentMap.toDiscover
        )
    }
}
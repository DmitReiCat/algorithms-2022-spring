package core

// Anything in the room
sealed class RoomContent

// Item is something player can take with
sealed class Item : RoomContent()

object Treasure : Item()
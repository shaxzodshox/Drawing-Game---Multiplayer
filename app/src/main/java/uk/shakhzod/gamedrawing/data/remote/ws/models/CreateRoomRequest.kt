package uk.shakhzod.gamedrawing.data.remote.ws.models

data class CreateRoomRequest(
    val name: String,
    val maxPlayers: Int
)
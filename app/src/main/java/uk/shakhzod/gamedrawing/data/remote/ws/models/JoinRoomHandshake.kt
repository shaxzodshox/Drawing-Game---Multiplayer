package uk.shakhzod.gamedrawing.data.remote.ws.models

import uk.shakhzod.gamedrawing.util.Constants.TYPE_JOIN_ROOM_HANDSHAKE

data class JoinRoomHandshake(
    val username: String,
    val roomName: String,
    val clientId: String
) : BaseModel(TYPE_JOIN_ROOM_HANDSHAKE)
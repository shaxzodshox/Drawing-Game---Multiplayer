package uk.shakhzod.gamedrawing.repository

import uk.shakhzod.gamedrawing.data.remote.ws.Room
import uk.shakhzod.gamedrawing.util.Resource

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

interface SetupRepository {
    suspend fun createRoom(room: Room) : Resource<Unit>
    suspend fun getRooms(searchQuery: String) : Resource<List<Room>>
    suspend fun joinRoom(username: String, roomName: String) : Resource<Unit>
}
package uk.shakhzod.gamedrawing.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import uk.shakhzod.gamedrawing.data.remote.responses.BasicApiResponse
import uk.shakhzod.gamedrawing.data.remote.ws.Room

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

interface SetupApi {
    @POST("/api/createRoom")
    suspend fun createRoom(
        @Body room: Room
    ) : Response<BasicApiResponse>

    @GET("/api/getRooms")
    suspend fun getRooms(
        @Query("searchQuery") searchQuery: String
    ) : Response<List<Room>>

    @GET("/api/joinRoom")
    suspend fun joinRoom(
        @Query("username") username: String,
        @Query("roomName") roomName: String
    ) : Response<BasicApiResponse>
}
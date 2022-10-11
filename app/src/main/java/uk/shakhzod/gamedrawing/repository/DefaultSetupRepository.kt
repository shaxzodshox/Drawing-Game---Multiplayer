package uk.shakhzod.gamedrawing.repository

import android.content.res.Resources
import androidx.annotation.StringRes
import retrofit2.HttpException
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.data.remote.api.SetupApi
import uk.shakhzod.gamedrawing.data.remote.ws.Room
import uk.shakhzod.gamedrawing.util.Resource
import uk.shakhzod.gamedrawing.util.connection.Connection
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

class DefaultSetupRepository @Inject constructor(
    private val setupApi: SetupApi,
    private val connection: Connection,
    private val resources: Resources
) : SetupRepository {

    override suspend fun createRoom(room: Room): Resource<Unit> {
        if (!connection.isConnected()) {
            return Resource.Error(string(R.string.error_internet_turned_off))
        }
        val response = try {
            setupApi.createRoom(room)
        } catch (e: HttpException) {
            return Resource.Error(string(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(string(R.string.check_internet_connection))
        }

        return if(response.isSuccessful && response.body()?.successful != true){
            Resource.Success(Unit)
        }else if(response.body()?.successful == false){
            Resource.Error(response.body()!!.message!!)
        } else{
            Resource.Error(string(R.string.error_unknown))
        }
    }

    override suspend fun getRooms(searchQuery: String): Resource<List<Room>> {
        if (!connection.isConnected()) {
            return Resource.Error(string(R.string.error_internet_turned_off))
        }
        val response = try {
            setupApi.getRooms(searchQuery)
        } catch (e: HttpException) {
            return Resource.Error(string(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(string(R.string.check_internet_connection))
        }

        return if(response.isSuccessful && response.body() != null){
            Resource.Success(response.body()!!)
        } else{
            Resource.Error(string(R.string.error_unknown))
        }
    }

    override suspend fun joinRoom(username: String, roomName: String): Resource<Unit> {
        if (!connection.isConnected()) {
            return Resource.Error(string(R.string.error_internet_turned_off))
        }
        val response = try {
            setupApi.joinRoom(username, roomName)
        } catch (e: HttpException) {
            return Resource.Error(string(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(string(R.string.check_internet_connection))
        }

        return if(response.isSuccessful && response.body()?.successful != true){
            Resource.Success(Unit)
        }else if(response.body()?.successful == false){
            Resource.Error(response.body()!!.message!!)
        } else{
            Resource.Error(string(R.string.error_unknown))
        }
    }

    private fun string(@StringRes id: Int): String {
        return resources.getString(id)
    }
}
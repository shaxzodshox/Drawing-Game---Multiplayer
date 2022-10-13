package uk.shakhzod.gamedrawing.data.remote.ws

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import uk.shakhzod.gamedrawing.data.remote.ws.models.BaseModel

/**
 * Created by Shakhzod Ilkhomov on 13/10/22
 **/

interface DrawingApi {
    @Receive
    fun observeEvents() : Flow<WebSocket.Event>

    @Send
    fun sendBaseModel(baseModel: BaseModel) : Boolean

    @Receive
    fun observeBaseModels() : Flow<BaseModel>
}
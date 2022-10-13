package uk.shakhzod.gamedrawing.data.remote.ws

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import uk.shakhzod.gamedrawing.data.remote.ws.models.*
import uk.shakhzod.gamedrawing.util.Constants.TYPE_ANNOUNCEMENT
import uk.shakhzod.gamedrawing.util.Constants.TYPE_CHAT_MESSAGE
import uk.shakhzod.gamedrawing.util.Constants.TYPE_CHOSEN_WORD
import uk.shakhzod.gamedrawing.util.Constants.TYPE_CUR_ROUND_DRAW_INFO
import uk.shakhzod.gamedrawing.util.Constants.TYPE_DISCONNECT_REQUEST
import uk.shakhzod.gamedrawing.util.Constants.TYPE_DRAW_ACTION
import uk.shakhzod.gamedrawing.util.Constants.TYPE_DRAW_DATA
import uk.shakhzod.gamedrawing.util.Constants.TYPE_GAME_ERROR
import uk.shakhzod.gamedrawing.util.Constants.TYPE_GAME_STATE
import uk.shakhzod.gamedrawing.util.Constants.TYPE_JOIN_ROOM_HANDSHAKE
import uk.shakhzod.gamedrawing.util.Constants.TYPE_NEW_WORDS
import uk.shakhzod.gamedrawing.util.Constants.TYPE_PHASE_CHANGE
import uk.shakhzod.gamedrawing.util.Constants.TYPE_PING
import uk.shakhzod.gamedrawing.util.Constants.TYPE_PLAYERS_LIST
import java.lang.reflect.Type

/**
 * Created by Shakhzod Ilkhomov on 13/10/22
 **/

@Suppress("UNCHECKED_CAST")
class CustomJsonMessageAdapter<T> private constructor(
    val gson: Gson
) : MessageAdapter<T> {
    override fun fromMessage(message: Message): T {
        val stringValue = when (message) {
            is Message.Text -> message.value
            is Message.Bytes -> message.value.toString()
        }
        val jsonObject = JsonParser.parseString(stringValue).asJsonObject

        val type = when(jsonObject.get("type").asString){
            TYPE_CHAT_MESSAGE -> ChatMessage::class.java
            TYPE_DRAW_DATA -> DrawData::class.java
            TYPE_ANNOUNCEMENT -> Announcement::class.java
            TYPE_JOIN_ROOM_HANDSHAKE -> JoinRoomHandshake::class.java
            TYPE_PHASE_CHANGE -> PhaseChange::class.java
            TYPE_CHOSEN_WORD -> ChosenWord::class.java
            TYPE_GAME_STATE -> GameState::class.java
            TYPE_PING -> Ping::class.java
            TYPE_DISCONNECT_REQUEST -> DisconnectRequest::class.java
            TYPE_DRAW_ACTION -> DrawAction::class.java
            TYPE_CUR_ROUND_DRAW_INFO -> RoundDrawInfo::class.java
            TYPE_GAME_ERROR -> GameError::class.java
            TYPE_NEW_WORDS -> NewWords::class.java
            TYPE_PLAYERS_LIST -> PlayersList::class.java
            else -> BaseModel::class.java
        }
        val obj = gson.fromJson(stringValue, type)
        return obj as T
    }

    override fun toMessage(data: T): Message {
       var convertedData = data as BaseModel
        convertedData = when(convertedData.type){
            TYPE_CHAT_MESSAGE -> convertedData as ChatMessage
            TYPE_DRAW_DATA -> convertedData as DrawData
            TYPE_ANNOUNCEMENT -> convertedData as Announcement
            TYPE_JOIN_ROOM_HANDSHAKE -> convertedData as JoinRoomHandshake
            TYPE_PHASE_CHANGE -> convertedData as PhaseChange
            TYPE_CHOSEN_WORD -> convertedData as ChosenWord
            TYPE_GAME_STATE -> convertedData as GameState
            TYPE_PING -> convertedData as Ping
            TYPE_DISCONNECT_REQUEST -> convertedData as DisconnectRequest
            TYPE_DRAW_ACTION -> convertedData as DrawAction
            TYPE_CUR_ROUND_DRAW_INFO -> convertedData as RoundDrawInfo
            TYPE_GAME_ERROR -> convertedData as GameError
            TYPE_NEW_WORDS -> convertedData as NewWords
            TYPE_PLAYERS_LIST -> convertedData as PlayersList
            else -> convertedData
        }
        return Message.Text(gson.toJson(convertedData))
    }
    class Factory(
        private val gson: Gson
    ) : MessageAdapter.Factory{
        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            return CustomJsonMessageAdapter<Any>(gson)
        }
    }
}
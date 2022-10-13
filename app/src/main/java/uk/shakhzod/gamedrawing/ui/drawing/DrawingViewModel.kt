package uk.shakhzod.gamedrawing.ui.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.data.remote.ws.DrawingApi
import uk.shakhzod.gamedrawing.data.remote.ws.models.*
import uk.shakhzod.gamedrawing.data.remote.ws.models.DrawAction.Companion.ACTION_UNDO
import uk.shakhzod.gamedrawing.util.DispatcherProvider
import javax.inject.Inject

/**
 * Created by Shakhzod Ilkhomov on 13/10/22
 **/

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val drawingApi: DrawingApi,
    private val dispatchers: DispatcherProvider,
    private val gson : Gson
) : ViewModel() {

    sealed class SocketEvent{
        data class ChatMessageEvent(val data: ChatMessage) : SocketEvent()
        data class AnnouncementEvent(val data: Announcement) : SocketEvent()
        data class GameStateEvent(val data: GameState): SocketEvent()
        data class DrawDataEvent(val data: DrawData) : SocketEvent()
        data class NewWordsEvent(val data: NewWords) : SocketEvent()
        data class ChosenWordEvent(val data: ChosenWord) : SocketEvent()
        data class GameErrorEvent(val data: GameError) : SocketEvent()
        data class RoundDrawInfoEvent(val data: List<BaseModel>) : SocketEvent()
        object UndoEvent : SocketEvent()
    }

    private val _selectedColorButtonId  = MutableStateFlow(R.id.rbBlack)
    val selectedColorButtonId: StateFlow<Int> = _selectedColorButtonId

    private val _connectionProgressBarVisible  = MutableStateFlow(true)
    val connectionProgressBarVisible: StateFlow<Boolean> = _connectionProgressBarVisible

    private val _chooseWordOverlayVisible  = MutableStateFlow(false)
    val chooseWordOverlayVisible: StateFlow<Boolean> = _chooseWordOverlayVisible

    private val connectionEventChannel = Channel<WebSocket.Event>()
    val connectionEvent = connectionEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    private val socketEventChannel = Channel<SocketEvent>()
    val socketEvent = socketEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    init {
        observeBaseModels()
        observeEvents()
    }

    fun setChooseWordOverlayVisibility(isVisible: Boolean){
        _chooseWordOverlayVisible.value = isVisible
    }

    fun setConnectionProgressBarVisibility(isVisible: Boolean){
        _connectionProgressBarVisible.value = isVisible
    }

    fun checkRadioButton(id: Int){
        _selectedColorButtonId.value = id
    }

    private fun observeEvents(){
        viewModelScope.launch(dispatchers.io) {
            drawingApi.observeEvents().collect{ event ->
                connectionEventChannel.send(event)
            }
        }
    }

    private fun observeBaseModels(){
        viewModelScope.launch(dispatchers.io) {
            drawingApi.observeBaseModels().collect{ data ->
                when(data){
                    is DrawData -> {
                        socketEventChannel.send(SocketEvent.DrawDataEvent(data))
                    }
                    is DrawAction -> {
                        when(data.action){
                            ACTION_UNDO -> socketEventChannel.send(SocketEvent.UndoEvent)
                        }
                    }
                    is GameError -> socketEventChannel.send(SocketEvent.GameErrorEvent(data))
                    is Ping -> sendBaseModel(Ping())
                }
            }
        }
    }

    fun sendBaseModel(data: BaseModel){
        viewModelScope.launch(dispatchers.io){
            drawingApi.sendBaseModel(data)
        }
    }
}
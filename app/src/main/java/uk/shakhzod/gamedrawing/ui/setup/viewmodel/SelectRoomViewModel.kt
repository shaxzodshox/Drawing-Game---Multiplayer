package uk.shakhzod.gamedrawing.ui.setup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import uk.shakhzod.gamedrawing.data.remote.ws.Room
import uk.shakhzod.gamedrawing.repository.SetupRepository
import uk.shakhzod.gamedrawing.util.Constants.MAX_ROOM_NAME_LENGTH
import uk.shakhzod.gamedrawing.util.Constants.MAX_USERNAME_LENGTH
import uk.shakhzod.gamedrawing.util.Constants.MIN_ROOM_NAME_LENGTH
import uk.shakhzod.gamedrawing.util.Constants.MIN_USERNAME_LENGTH
import uk.shakhzod.gamedrawing.util.DispatcherProvider
import uk.shakhzod.gamedrawing.util.Resource
import javax.inject.Inject

/**
 * Created by Shakhzod Ilkhomov on 12/10/22
 **/

@HiltViewModel
class SelectRoomViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel(){

    sealed class SetupEvent{
        data class GetRoomEvent(val rooms: List<Room>) : SetupEvent()
        data class GetRoomErrorEvent(val error: String) : SetupEvent()
        object GetRoomLoadingEvent : SetupEvent()
        object GetRoomEmptyEvent : SetupEvent()

        data class JoinRoomEvent(val roomName: String) : SetupEvent()
        data class JoinRoomErrorEvent(val error: String) : SetupEvent()
    }

    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent : SharedFlow<SetupEvent> = _setupEvent

    private val _rooms = MutableStateFlow<SetupEvent>(SetupEvent.GetRoomEmptyEvent)
    val rooms : SharedFlow<SetupEvent> = _rooms

    fun getRooms(searchQuery: String){
        _rooms.value = SetupEvent.GetRoomLoadingEvent
        viewModelScope.launch(dispatchers.main) {
            //Although the dispatcher is main, retrofit always works in background thread under the hood
            val result = repository.getRooms(searchQuery)
            if(result is Resource.Success){
                _rooms.value = SetupEvent.GetRoomEvent(result.data ?: return@launch)
            }
            else{
                _setupEvent.emit(SetupEvent.GetRoomErrorEvent(result.message ?: return@launch))
            }
        }
    }

    fun joinRoom(username: String, roomName: String){
        _rooms.value = SetupEvent.GetRoomLoadingEvent
        viewModelScope.launch(dispatchers.main) {
            //Although the dispatcher is main, retrofit always works in background thread under the hood
            val result = repository.joinRoom(username, roomName)
            if(result is Resource.Success){
                _setupEvent.emit(SetupEvent.JoinRoomEvent(roomName))
            }
            else{
                _setupEvent.emit(SetupEvent.JoinRoomErrorEvent(result.message ?: return@launch))
            }
        }
    }
}
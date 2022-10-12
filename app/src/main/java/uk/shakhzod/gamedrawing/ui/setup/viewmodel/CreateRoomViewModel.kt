package uk.shakhzod.gamedrawing.ui.setup.viewmodel

/**
 * Created by Shakhzod Ilkhomov on 12/10/22
 **/

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import uk.shakhzod.gamedrawing.data.remote.ws.Room
import uk.shakhzod.gamedrawing.repository.SetupRepository
import uk.shakhzod.gamedrawing.util.Constants.MAX_ROOM_NAME_LENGTH
import uk.shakhzod.gamedrawing.util.Constants.MIN_ROOM_NAME_LENGTH
import uk.shakhzod.gamedrawing.util.DispatcherProvider
import uk.shakhzod.gamedrawing.util.Resource
import javax.inject.Inject

/**
 * Created by Shakhzod Ilkhomov on 12/10/22
 **/

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel(){

    sealed class SetupEvent{
        object InputEmptyError : SetupEvent()
        object InputTooShortError : SetupEvent()
        object InputTooLongError : SetupEvent()

        data class CreateRoomEvent(val room: Room) : SetupEvent()
        data class CreateRoomErrorEvent(val error : String) : SetupEvent()

        data class JoinRoomEvent(val roomName: String) : SetupEvent()
        data class JoinRoomErrorEvent(val error: String) : SetupEvent()
    }

    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent : SharedFlow<SetupEvent> = _setupEvent

    fun createRoom(room: Room){
        viewModelScope.launch(dispatchers.main) {
            val trimmedRoomName = room.name.trim()
            when{
                trimmedRoomName.isEmpty() -> {
                    _setupEvent.emit(SetupEvent.InputEmptyError)
                }
                trimmedRoomName.length < MIN_ROOM_NAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooShortError)
                }
                trimmedRoomName.length > MAX_ROOM_NAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooLongError)
                }
                else -> {
                    val result = repository.createRoom(room)
                    if(result is Resource.Success){
                        _setupEvent.emit(SetupEvent.CreateRoomEvent(room))
                    }
                    else{
                        _setupEvent.emit(
                            SetupEvent.CreateRoomErrorEvent(
                                result.message ?: return@launch
                            )
                        )
                    }
                }
            }
        }
    }

    fun joinRoom(username: String, roomName: String){
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
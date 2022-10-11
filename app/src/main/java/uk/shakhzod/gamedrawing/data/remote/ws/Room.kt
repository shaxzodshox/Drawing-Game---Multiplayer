package uk.shakhzod.gamedrawing.data.remote.ws

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

data class Room(
    val name: String,
    val maxPlayers: Int,
    val playerCount: Int = 1
){
    enum class Phase {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_START,
        NEW_ROUND,
        GAME_RUNNING,
        SHOW_WORD
    }
}

package uk.shakhzod.gamedrawing.data.remote.ws.models

import uk.shakhzod.gamedrawing.util.Constants.TYPE_PLAYERS_LIST

data class PlayersList(
    val players: List<PlayerData>
) : BaseModel(TYPE_PLAYERS_LIST)

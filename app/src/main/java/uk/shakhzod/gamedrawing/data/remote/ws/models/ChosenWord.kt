package uk.shakhzod.gamedrawing.data.remote.ws.models

import uk.shakhzod.gamedrawing.util.Constants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String
) : BaseModel(TYPE_CHOSEN_WORD)

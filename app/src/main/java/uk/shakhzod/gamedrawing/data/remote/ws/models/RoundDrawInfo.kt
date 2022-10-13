package uk.shakhzod.gamedrawing.data.remote.ws.models

import uk.shakhzod.gamedrawing.util.Constants.TYPE_CUR_ROUND_DRAW_INFO

data class RoundDrawInfo(
    val data: List<String>
) : BaseModel(TYPE_CUR_ROUND_DRAW_INFO)

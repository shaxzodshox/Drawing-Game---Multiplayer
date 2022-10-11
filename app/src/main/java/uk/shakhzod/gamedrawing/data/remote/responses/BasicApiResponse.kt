package uk.shakhzod.gamedrawing.data.remote.responses

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

data class BasicApiResponse(
    val successful: Boolean,
    val message: String? = null
)

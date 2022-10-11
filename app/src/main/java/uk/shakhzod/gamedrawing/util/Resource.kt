package uk.shakhzod.gamedrawing.util

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

sealed class Resource<T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

}

package uk.shakhzod.gamedrawing.util.connection

import android.content.Context

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

class Connection(private val context: Context) {
    fun isConnected() : Boolean = context.checkForInternetConnection()
}
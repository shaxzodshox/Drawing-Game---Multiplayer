package uk.shakhzod.gamedrawing.util

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
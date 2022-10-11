package uk.shakhzod.gamedrawing

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

@HiltAndroidApp
class DrawingApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}
package uk.shakhzod.gamedrawing.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

val Context.datastore by preferencesDataStore("settings")
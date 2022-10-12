package uk.shakhzod.gamedrawing.util.extensions

import android.view.View

/**
 * Created by Shakhzod Ilkhomov on 12/10/22
 **/

inline fun View.onClick(crossinline block : (View) -> Unit){
    this.setOnClickListener {
        block(it)
    }
}
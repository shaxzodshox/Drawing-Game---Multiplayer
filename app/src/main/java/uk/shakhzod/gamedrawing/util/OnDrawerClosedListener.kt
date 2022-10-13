package uk.shakhzod.gamedrawing.util

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener

/**
 * Created by Shakhzod Ilkhomov on 13/10/22
 **/

fun interface OnDrawerClosedListener : DrawerListener {
    override fun onDrawerOpened(drawerView: View) = Unit
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
    override fun onDrawerStateChanged(newState: Int) = Unit
}
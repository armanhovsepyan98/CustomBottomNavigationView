package com.app.custombottomnavigatiomview.bottom_navigation_components

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.custombottomnavigatiomview.R
import com.app.custombottomnavigatiomview.interfaces.OnBottomNavItemClickedListener

class BottomNavigationViewItem(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val navItemLinear by lazy { findViewById<View>(R.id.navItemLinear) }
    private val navItemIV by lazy { findViewById<ImageView>(R.id.navItemIV) }
    private val navItemTV by lazy { findViewById<TextView>(R.id.navItemTV) }

    var isItemSelected = false

    var index: Int = 0
    var listener: OnBottomNavItemClickedListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.button_nav_item, this, true)
        setListener()
    }

    private fun setListener() {
        navItemLinear.contentDescription = index.toString()
        navItemLinear.setOnClickListener {
            listener?.onItemClicked(index)
        }
    }

    fun select() {
        isItemSelected = true
        navItemIV?.background?.colorFilter =
            PorterDuffColorFilter(resources.getColor(R.color.selected_nav_item_color), PorterDuff.Mode.SRC_ATOP)
        navItemTV.setTextColor(resources.getColor(R.color.selected_nav_item_color))
    }

    fun unSelect() {
        isItemSelected = false
        navItemIV?.background?.colorFilter =
            PorterDuffColorFilter(
                resources.getColor(R.color.navBarItemColor),
                PorterDuff.Mode.SRC_ATOP
            )
        navItemTV.setTextColor(resources.getColor(R.color.navBarItemColor))
    }
}

package com.app.custombottomnavigatiomview.bottom_navigation_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.custombottomnavigatiomview.R
import com.app.custombottomnavigatiomview.interfaces.OnBottomNavItemClickedListener
import com.app.custombottomnavigatiomview.model.BottomNavigationData

class BottomNavigationBar(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    private val customNavBar by lazy { findViewById<CurvedBottomNavigationView>(R.id.customNavBar) }
    private var data = arrayOf<BottomNavigationData>()

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_navigation_bar, this, true)
    }

    fun bindData(data: Array<BottomNavigationData>) {
        this.data = data
        customNavBar.bind(data)
    }

    fun selectItemAtIndex(index: Int) {
        customNavBar.selectItemAtIndex(index)
    }

    fun setOnBottomNavItemClickedListener(listener: OnBottomNavItemClickedListener) {
        customNavBar.setOnBottomNavItemClickedListener(listener)
    }
}
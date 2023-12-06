package com.app.custombottomnavigatiomview.bottom_navigation_components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.app.custombottomnavigatiomview.R
import com.app.custombottomnavigatiomview.interfaces.OnFabClickListener

class BottomNavigationFloatingButton(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val createFab by lazy { findViewById<View>(R.id.createFab) }
    private val textFab by lazy { findViewById<View>(R.id.textFab) }
    private val micFab by lazy { findViewById<View>(R.id.micFab) }
    private val cameraFab by lazy { findViewById<View>(R.id.cameraFab) }
    var onSmallFabClickListener: OnFabClickListener.OnSmallFabClickListener? = null
    var onBigFabClickListener: OnFabClickListener.OnBigFabClickListener? = null
    var clicked = false


    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_botton_anim
        )
    }

    fun setFabClickListener(onSmallFabClickListener: OnFabClickListener.OnSmallFabClickListener) {
        this.onSmallFabClickListener = onSmallFabClickListener
    }
    fun setBigFabClickListener(onBigFabClickListener: OnFabClickListener.OnBigFabClickListener) {
        this.onBigFabClickListener = onBigFabClickListener
    }

    init {
        inflate(context, R.layout.floating_button_layout, this)

        createFab.setOnClickListener {
            onAddButtonClicked()
        }
        textFab.setOnClickListener {
            onSmallFabClickListener?.onSmallFabClick(FabType.CAMERA.ordinal)
        }
        cameraFab.setOnClickListener {
            onSmallFabClickListener?.onSmallFabClick(FabType.MIC.ordinal)
        }
        micFab.setOnClickListener {
            onSmallFabClickListener?.onSmallFabClick(FabType.TEXT.ordinal)
        }
    }

    fun disableFab(mod: Boolean) {
        clicked = !mod
        setAnimation(!clicked)
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        onBigFabClickListener?.onBigFabClick(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            textFab.visibility = View.VISIBLE
            cameraFab.visibility = View.VISIBLE
            micFab.visibility = View.VISIBLE
            setFabButtonsEnable()
        } else {
            textFab.visibility = View.INVISIBLE
            cameraFab.visibility = View.INVISIBLE
            micFab.visibility = View.INVISIBLE
            setFabButtonsDisable()
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            textFab.startAnimation(fromBottom)
            cameraFab.startAnimation(fromBottom)
            micFab.startAnimation(fromBottom)
            createFab.startAnimation(rotateOpen)
            setFabButtonsEnable()
        } else {
            textFab.startAnimation(toBottom)
            cameraFab.startAnimation(toBottom)
            micFab.startAnimation(toBottom)
            createFab.startAnimation(rotateClose)
            setFabButtonsDisable()
        }
    }

    private fun setFabButtonsEnable() {
        textFab.isEnabled = true
        cameraFab.isEnabled = true
        micFab.isEnabled = true
    }

    private fun setFabButtonsDisable() {
        textFab.isEnabled = false
        cameraFab.isEnabled = false
        micFab.isEnabled = false
    }
}

enum class FabType(i: Int) {
    TEXT(0),
    CAMERA(1),
    MIC(2)
}
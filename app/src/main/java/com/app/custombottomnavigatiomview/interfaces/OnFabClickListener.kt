package com.app.custombottomnavigatiomview.interfaces

interface OnFabClickListener {

    interface OnSmallFabClickListener {
        fun onSmallFabClick(position: Int)
    }

    interface OnBigFabClickListener {
        fun onBigFabClick(state: Boolean)
    }

}
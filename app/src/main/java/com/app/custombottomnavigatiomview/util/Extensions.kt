package com.app.custombottomnavigatiomview.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.app.custombottomnavigatiomview.R
import kotlin.math.abs

fun Context.dpToPx(src: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, src.toFloat(), resources.displayMetrics)
        .toInt()
}

fun View.dpToPx(src: Int) = context.dpToPx(src)

fun View.onClick(function: () -> Unit): View {

    var previousClickTimestamp: Long = 0

    setOnClickListener {
        val currentTimeStamp = System.currentTimeMillis()
        val lastTimeStamp = previousClickTimestamp
        previousClickTimestamp = currentTimeStamp
        if (abs(currentTimeStamp - lastTimeStamp) > 300) {
            function()
        }
    }
    return this
}

fun delayed(milliseconds: Long, body: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(body, milliseconds)
}

inline fun <reified FRAGMENT : Fragment> FragmentActivity.showFragment(animate: Boolean = true,
                                                                       backStack: Boolean = false,
                                                                       vararg arguments: Pair<String, Any>) {
    showFragment<FRAGMENT>(this, supportFragmentManager, animate, backStack, arguments = arguments)
}

inline fun <reified FRAGMENT> showFragment(context: Context, manager: FragmentManager,
                                           animate: Boolean, backStack: Boolean,
                                           vararg arguments: Pair<String, Any>) {
    val tag = FRAGMENT::class.java.name
    var fragment = manager.findFragmentByTag(tag)
    if (fragment != null) {
        inTransaction(manager, animate) {
            val currentFragment = fragment ?: return
            show(currentFragment)
        }
    } else {
        fragment = manager.fragmentFactory.instantiate(context.classLoader, tag)
        fragment.arguments = bundleOf(*arguments)
        inTransaction(manager, animate) {
            add(R.id.fragmentContainer, fragment, tag)
            if (backStack)
                addToBackStack(tag)
        }
    }
    if (!backStack) {
        manager.fragments
            .filter { it.tag != null }
            .filter { it.tag != fragment.tag }
            .forEach {
                inTransaction(manager, animate) {
                    hide(it)
                }
            }
    }
}

inline fun inTransaction(fragmentManager: FragmentManager, animate: Boolean, transaction: FragmentTransaction.() -> Unit) {
    fragmentManager.commitNow(true) {
        if (animate)
            setCustomAnimations(
                R.anim.fragment_open_enter, R.anim.fragment_close_exit,
                R.anim.fragment_open_enter, R.anim.fragment_open_exit)
        transaction()
    }
}
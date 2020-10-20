package com.cosa.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat


class OverScrollBehavior(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<View>() {
    private var mOverScrollY = 0

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        mOverScrollY = 0
        return  true
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyUnconsumed == 0) {
            return
        }

        mOverScrollY -= dyUnconsumed
        val group = target as ViewGroup
        val count = group.childCount
        for (i in 0 until count) {
            val view = group.getChildAt(i)
            view.translationY = mOverScrollY.toFloat()
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        val group: ViewGroup = target as ViewGroup
        val count:Int = group.childCount
        for (i in 0 until  count){
            val view:View = group.getChildAt(i)
            ViewCompat.animate(view).setDuration(500).translationY(0F).start()
        }
    }
}
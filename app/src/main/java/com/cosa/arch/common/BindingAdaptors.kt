package com.cosa.arch.common

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

object BindingAdaptors {
    @JvmStatic
    @BindingAdapter(
        value = ["swipeEnabled", "bgColorSwipeRight", "bgColorSwipeLeft", "onItemSwipeRight", "onItemSwipeLeft"],
        requireAll = false
    )
    fun setItemSwipeToRecyclerView(
        recyclerView: RecyclerView,
        swipeEnabled: Boolean,
        bgColorSwipeRight: Int,
        bgColorSwipeLeft: Int,
        onItemSwipeRight: SwipeItemTouchHelper.OnItemSwipeListener,
        onItemSwipeLeft: SwipeItemTouchHelper.OnItemSwipeListener
    ) {
        val swipeCallback: ItemTouchHelper.Callback =
            SwipeItemTouchHelper.Builder(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                .bgColorSwipeRight(bgColorSwipeRight)
                .bgColorSwipeLeft(bgColorSwipeLeft)
                .setSwipeEnabled(swipeEnabled)
                .onItemSwipeLeftListener(onItemSwipeLeft)
                .onItemSwipeRightListener(onItemSwipeRight)
                .build()
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
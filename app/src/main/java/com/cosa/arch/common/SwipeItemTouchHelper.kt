package com.cosa.arch.common

import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeItemTouchHelper(dragDir: Int, swipeDir: Int) :
    ItemTouchHelper.SimpleCallback(dragDir, swipeDir) {
    private lateinit var onItemSwipeRightListener: OnItemSwipeListener
    private lateinit var onItemSwipeLeftListener: OnItemSwipeListener
    private var paintRight: Paint? = null
    private var paintLeft: Paint? = null
    private var swipeEnabled: Boolean = false

    private constructor(builder: Builder) : this(builder.dragDirs, builder.swipeDirs) {
        paintRight = Paint(Paint.ANTI_ALIAS_FLAG)
        paintLeft = Paint(Paint.ANTI_ALIAS_FLAG)
        setPaintColor(paintRight!!, builder.bgColorSwipeRight)
        setPaintColor(paintLeft!!, builder.bgColorSwipeLeft)
        swipeEnabled = builder.swipeEnabled
        onItemSwipeRightListener = builder.onItemSwipeRightListener!!
        onItemSwipeLeftListener = builder.onItemSwipeLeftListener!!
    }

    private fun setPaintColor(paint: Paint, color: Int) {
        paint.color = color
    }


    override fun isItemViewSwipeEnabled(): Boolean {
        return swipeEnabled
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.RIGHT -> {
                onItemSwipeRightListener.onItemSwiped(position)
            }
            ItemTouchHelper.LEFT -> {
                onItemSwipeLeftListener.onItemSwiped(position)
            }
        }
    }

    interface OnItemSwipeListener {
        fun onItemSwiped(position: Int)
    }

    class Builder(val dragDirs: Int, val swipeDirs: Int) {
        var bgColorSwipeRight: Int = 0
        var bgColorSwipeLeft: Int = 0
        var onItemSwipeRightListener: OnItemSwipeListener? = null
        var onItemSwipeLeftListener: OnItemSwipeListener? = null
        var swipeEnabled: Boolean = false

        fun bgColorSwipeRight(value: Int): Builder {
            bgColorSwipeRight = value
            return this
        }

        fun bgColorSwipeLeft(value: Int): Builder {
            bgColorSwipeLeft = value
            return this
        }

        fun onItemSwipeRightListener(value: OnItemSwipeListener): Builder {
            onItemSwipeRightListener = value
            return this
        }

        fun onItemSwipeLeftListener(value: OnItemSwipeListener): Builder {
            onItemSwipeLeftListener = value
            return this
        }

        fun setSwipeEnabled(value: Boolean): Builder {
            swipeEnabled = value
            return this
        }

        fun build(): SwipeItemTouchHelper {
            return SwipeItemTouchHelper(this)
        }
    }
}
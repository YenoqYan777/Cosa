package com.example.cosa.arch.helpers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeItemTouchHelper(dragDir: Int, swipeDir: Int) :
    ItemTouchHelper.SimpleCallback(dragDir, swipeDir) {
    private lateinit var drawableRight: Drawable
    private lateinit var drawableLeft: Drawable
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
        drawableRight = builder.drawableSwipeRight!!
        drawableLeft = builder.drawableSwipeLeft!!
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

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width = height / 3
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface OnItemSwipeListener {
        fun onItemSwiped(position: Int)
    }

    class Builder(val dragDirs: Int, val swipeDirs: Int) {
        var drawableSwipeRight: Drawable? = null
        var drawableSwipeLeft: Drawable? = null
        var bgColorSwipeRight: Int = 0
        var bgColorSwipeLeft: Int = 0
        var onItemSwipeRightListener: OnItemSwipeListener? = null
        var onItemSwipeLeftListener: OnItemSwipeListener? = null
        var swipeEnabled: Boolean = false

        fun drawableSwipeRight(value: Drawable): Builder {
            drawableSwipeRight = value
            return this
        }

        fun drawableSwipeLeft(value: Drawable): Builder {
            drawableSwipeLeft = value
            return this
        }

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
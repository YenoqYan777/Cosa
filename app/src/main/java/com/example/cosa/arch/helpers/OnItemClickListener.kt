package com.example.cosa.arch.helpers

import android.view.View

interface OnItemClickListener {
    fun onItemClick(position: Int, view: View)
    fun onWholeItemClick(position: Int, view: View)
}
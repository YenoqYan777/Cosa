package com.cosa.arch.things.adapters

import androidx.recyclerview.widget.DiffUtil
import com.cosa.models.Things

class ThingsDiffCallBack : DiffUtil.Callback() {
    private lateinit var oldList: MutableList<Things>
    private lateinit var newList: MutableList<Things>

    fun setItems(oldList: MutableList<Things>, newList: MutableList<Things>) {
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
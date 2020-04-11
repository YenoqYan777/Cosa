package com.example.cosa.arch.thingAdded.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.cosa.models.ThingAdded

class ThingDiffCallBack : DiffUtil.Callback() {
    private lateinit var oldList: MutableList<ThingAdded>
    private lateinit var newList: MutableList<ThingAdded>

    fun setItems(oldList: MutableList<ThingAdded>, newList: MutableList<ThingAdded>) {
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
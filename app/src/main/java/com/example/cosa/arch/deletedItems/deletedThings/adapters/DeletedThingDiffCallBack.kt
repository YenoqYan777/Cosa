package com.example.cosa.arch.deletedItems.deletedThings.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.models.ThingAdded

class DeletedThingDiffCallBack : DiffUtil.Callback() {
    private lateinit var oldList: MutableList<DeletedThingAdded>
    private lateinit var newList: MutableList<DeletedThingAdded>

    fun setItems(oldList: MutableList<DeletedThingAdded>, newList: MutableList<DeletedThingAdded>) {
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
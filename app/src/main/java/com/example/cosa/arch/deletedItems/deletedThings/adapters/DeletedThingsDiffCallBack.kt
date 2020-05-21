package com.example.cosa.arch.deletedItems.deletedThings.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.cosa.models.DeletedThings

class DeletedThingsDiffCallBack : DiffUtil.Callback() {
    private lateinit var oldList: MutableList<DeletedThings>
    private lateinit var newList: MutableList<DeletedThings>

    fun setItems(oldList: MutableList<DeletedThings>, newList: MutableList<DeletedThings>) {
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
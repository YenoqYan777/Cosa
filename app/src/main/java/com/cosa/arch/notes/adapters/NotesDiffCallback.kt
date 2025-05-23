package com.cosa.arch.notes.adapters

import androidx.recyclerview.widget.DiffUtil
import com.cosa.models.Notes

class NotesDiffCallback : DiffUtil.Callback() {
    private lateinit var oldList: MutableList<Notes>
    private lateinit var newList: MutableList<Notes>

    fun setItems(oldList: MutableList<Notes>, newList: MutableList<Notes>) {
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
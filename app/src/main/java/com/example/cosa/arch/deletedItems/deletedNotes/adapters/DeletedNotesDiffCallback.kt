package com.example.cosa.arch.deletedItems.deletedNotes.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.cosa.models.DeletedNotes
import com.example.cosa.models.Notes

class DeletedNotesDiffCallback : DiffUtil.Callback() {
    private lateinit var oldList: MutableList<DeletedNotes>
    private lateinit var newList: MutableList<DeletedNotes>

    fun setItems(newList: MutableList<DeletedNotes>, oldList: MutableList<DeletedNotes>) {
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
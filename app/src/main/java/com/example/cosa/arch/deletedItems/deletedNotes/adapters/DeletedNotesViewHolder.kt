package com.example.cosa.arch.deletedItems.deletedNotes.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cosa.arch.deletedItems.deletedNotes.DeletedNotesViewModel
import com.example.cosa.arch.thingAdded.ThingAddedViewModel
import com.example.cosa.databinding.ItemDeletedNotesBinding
import com.example.cosa.databinding.ItemThingaddedBinding
import com.example.cosa.models.DeletedNotes
import com.example.cosa.models.ThingAdded
import kotlinx.android.synthetic.main.item_deleted_notes.view.*
import kotlinx.android.synthetic.main.item_thingadded.view.*

class DeletedNotesViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemDeletedNotesBinding = ItemDeletedNotesBinding.bind(itemView)
    fun bind(
        deletedNotes: DeletedNotes,
        viewModel: DeletedNotesViewModel
    ) {
        binding.delNotes = deletedNotes
        binding.delNotesViewModel = viewModel
        binding.delItem = binding.root.cvDelNoteItem

    }
}
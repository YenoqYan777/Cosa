package com.cosa.arch.deletedItems.deletedNotes.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cosa.arch.deletedItems.deletedNotes.DeletedNotesViewModel
import com.cosa.databinding.ItemDeletedNotesBinding
import com.cosa.models.DeletedNotes
import kotlinx.android.synthetic.main.item_deleted_notes.view.*

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
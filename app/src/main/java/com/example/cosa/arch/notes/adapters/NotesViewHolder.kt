package com.example.cosa.arch.notes.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cosa.arch.notes.NotesViewModel
import com.example.cosa.databinding.ItemNotesBinding
import com.example.cosa.models.Notes
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemNotesBinding = ItemNotesBinding.bind(itemView)
    fun bind(
        notes: Notes,
        viewModel: NotesViewModel
    ) {
        binding.notes = notes
        binding.viewModel = viewModel
        binding.item = binding.root.cvNoteItem
    }
}

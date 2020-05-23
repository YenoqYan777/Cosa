package com.example.cosa.arch.deletedItems.deletedNotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cosa.R
import com.example.cosa.arch.common.OnItemClickListener
import com.example.cosa.arch.deletedItems.deletedNotes.DeletedNotesViewModel
import com.example.cosa.arch.notes.NotesViewModel
import com.example.cosa.arch.notes.adapters.NotesViewHolder
import com.example.cosa.models.DeletedNotes
import com.example.cosa.models.Notes
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class DeletedNotesAdapter(
    private val notesDiffCallback: DeletedNotesDiffCallback,
    private val context: Context,
    private val viewModel: DeletedNotesViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var originalItems: MutableList<DeletedNotes> = ArrayList()
    private var filteredItems: MutableList<DeletedNotes> = ArrayList()
    private var lastPosition = -1

    fun getData(): MutableList<DeletedNotes> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeletedNotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_deleted_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DeletedNotesViewHolder -> {
                holder.bind(filteredItems[position], viewModel)
                setAnimation(holder.itemView, position)
            }
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    fun setOriginalItems(originalList: MutableList<DeletedNotes>) {
        originalItems.clear()
        originalItems.addAll(originalList)
        originalItems.sortWith(Comparator { o1, o2 -> o1.id.compareTo(o2.id) })
        submitList(originalList)
    }

    private fun submitList(notesList: MutableList<DeletedNotes>) {
        notesDiffCallback.setItems(filteredItems, notesList)
        val result = DiffUtil.calculateDiff(notesDiffCallback)
        filteredItems.clear()
        filteredItems.addAll(notesList)
        result.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val list = ArrayList<DeletedNotes>()
                if (constraint.isNullOrEmpty()) {
                    list.addAll(originalItems)
                } else {
                    val filteredPattern = constraint.toString().toLowerCase(Locale.getDefault())
                        .trim()
                    originalItems.forEach {
                        if (it.text.toLowerCase(Locale.getDefault()).contains(filteredPattern)) {
                            list.add(it)
                        }
                    }
                }
                val result = FilterResults()
                result.values = list
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results!!.values as MutableList<DeletedNotes>)
            }
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}
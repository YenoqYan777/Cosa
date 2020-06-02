package com.cosa.arch.notes.adapters

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
import com.cosa.R
import com.cosa.arch.notes.NotesViewModel
import com.cosa.models.Notes
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class NotesAdapter(
    private val notesDiffCallback: NotesDiffCallback,
    private val context: Context,
    private val viewModel: NotesViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var originalItems: MutableList<Notes> = ArrayList()
    private var filteredItems: MutableList<Notes> = ArrayList()

    fun getData(): MutableList<Notes> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotesViewHolder -> {
                holder.bind(filteredItems[position], viewModel)
                setAnimation(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val list = ArrayList<Notes>()
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
                submitList(results!!.values as MutableList<Notes>)
            }
        }
    }

    fun setOriginalItems(originalList: MutableList<Notes>) {
        originalItems.clear()
        originalItems.addAll(originalList)
        originalItems.sortWith(Comparator { o1, o2 -> o1.id.compareTo(o2.id) })
        submitList(originalList)
    }

    private fun submitList(notesList: MutableList<Notes>) {
        notesDiffCallback.setItems(filteredItems, notesList)
        val result = DiffUtil.calculateDiff(notesDiffCallback)
        filteredItems.clear()
        filteredItems.addAll(notesList)
        result.dispatchUpdatesTo(this)
    }

    private fun setAnimation(viewToAnimate: View) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.item_animation_bottom_slide)
            viewToAnimate.startAnimation(animation)
    }
}
package com.example.cosa.arch.notes.adapters

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
import com.example.cosa.arch.helpers.OnItemClickListener
import com.example.cosa.models.Notes
import kotlinx.android.synthetic.main.item_notes.view.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class NotesAdapter(
    private val notesDiffCallback: NotesDiffCallback,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var originalItems: MutableList<Notes> = ArrayList()
    private var filteredItems: MutableList<Notes> = ArrayList()
    private var lastPosition = -1
    private lateinit var clickListener: OnItemClickListener

    fun getData(): MutableList<Notes> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotesViewHolder -> {
                holder.bind(filteredItems[position], clickListener)
                setAnimation(holder.itemView, position)
            }
        }
    }

    override fun getItemCount(): Int = filteredItems.size

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
                        if (it.text.toLowerCase(Locale.getDefault()).startsWith(filteredPattern)) {
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

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class NotesViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val noteText = itemView.txtNotePreview
        fun bind(
            notes: Notes,
            listener: OnItemClickListener
        ) {
            noteText.text = notes.text
            noteText.setOnClickListener {
                listener.onItemClick(adapterPosition, it)
            }
        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        clickListener = itemClickListener
    }
}
package com.cosa.arch.deletedItems.deletedThings.adapters

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
import com.cosa.arch.deletedItems.deletedThings.DeletedThingsViewModel
import com.cosa.models.DeletedThings
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class DeletedThingsAdapter(
    private val thingsDiffCallBack: DeletedThingsDiffCallBack,
    private val context: Context,
    private val viewModel: DeletedThingsViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var originalItems: MutableList<DeletedThings> = ArrayList()
    private var filteredItems: MutableList<DeletedThings> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeletedThingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_deleted_things, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DeletedThingsViewHolder -> {
                holder.bind(filteredItems[position], viewModel)
                setAnimation(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val list = ArrayList<DeletedThings>()
                if (constraint.isNullOrEmpty()) {
                    list.addAll(originalItems)
                } else {
                    val filteredPattern = constraint.toString().toLowerCase(Locale.getDefault())
                        .trim()
                    originalItems.forEach {
                        if (it.thing.toLowerCase(Locale.getDefault())
                                .contains(filteredPattern)
                        ) {
                            list.add(it)
                        } else if (it.place.toLowerCase(Locale.getDefault())
                                .contains(filteredPattern)
                        ) {
                            list.add(it)
                        }
                    }
                }
                val result = FilterResults()
                result.values = list
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results!!.values as MutableList<DeletedThings>)
            }
        }
    }

    fun setOriginalItems(originalList: MutableList<DeletedThings>) {
        originalItems.clear()
        originalItems.addAll(originalList)
        originalItems.sortWith(Comparator { o1, o2 -> o1.id.compareTo(o2.id) })
        submitList(originalList)
    }

    private fun submitList(thingList: MutableList<DeletedThings>) {
        thingsDiffCallBack.setItems(filteredItems, thingList)
        val result = DiffUtil.calculateDiff(thingsDiffCallBack)
        filteredItems.clear()
        filteredItems.addAll(thingList)
        result.dispatchUpdatesTo(this)
    }

    private fun setAnimation(viewToAnimate: View) {
        val animation: Animation =
            AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)
        viewToAnimate.startAnimation(animation)
    }
}
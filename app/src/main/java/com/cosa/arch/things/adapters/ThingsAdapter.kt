package com.cosa.arch.things.adapters

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
import com.cosa.arch.things.ThingsViewModel
import com.cosa.models.Things
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class ThingsAdapter(
    private val thingsDiffCallBack: ThingsDiffCallBack,
    private val context: Context,
    private val viewModel: ThingsViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var originalItems: MutableList<Things> = ArrayList()
    private var filteredItems: MutableList<Things> = ArrayList()

    fun getData(): MutableList<Things> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_things, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ThingsViewHolder -> {
                holder.bind(filteredItems[position], viewModel)
                setAnimation(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val list = ArrayList<Things>()
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
                submitList(results!!.values as MutableList<Things>)
            }
        }
    }

    fun setOriginalItems(originalList: MutableList<Things>) {
        originalItems.clear()
        originalItems.addAll(originalList)
        originalItems.sortWith(Comparator { o1, o2 -> o1.id.compareTo(o2.id) })
        submitList(originalList)
    }

    private fun submitList(thingList: MutableList<Things>) {
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
package com.example.cosa.arch.deletedItems.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cosa.R
import com.example.cosa.arch.helpers.OnItemClickListener
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.repository.CacheStore
import kotlinx.android.synthetic.main.item_thingadded.view.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class DeletedThingAddedAdapter(
    private val thingDiffCallBack: DeletedThingDiffCallBack,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var originalItems: MutableList<DeletedThingAdded> = ArrayList()
    private var filteredItems: MutableList<DeletedThingAdded> = ArrayList()
    private var lastPosition = -1
    private lateinit var clickListener: OnItemClickListener

    fun getData(): MutableList<DeletedThingAdded> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThingAddedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_thingadded, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ThingAddedViewHolder -> {
                holder.bind(filteredItems[position], clickListener)
                setAnimation(holder.itemView, position)
            }
        }
    }

    override fun getItemCount(): Int = filteredItems.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val list = ArrayList<DeletedThingAdded>()
                if (constraint.isNullOrEmpty()) {
                    list.addAll(originalItems)
                } else {
                    val filteredPattern = constraint.toString().toLowerCase(Locale.getDefault())
                        .trim()
                    originalItems.forEach {
                        if (it.thing.toLowerCase(Locale.getDefault())
                                .startsWith(filteredPattern)
                        ) {
                            list.add(it)
                        } else if (it.place.toLowerCase(Locale.getDefault())
                                .startsWith(filteredPattern)
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
                submitList(results!!.values as MutableList<DeletedThingAdded>)
            }
        }
    }

    fun setOriginalItems(originalList: MutableList<DeletedThingAdded>) {
        originalItems.clear()
        originalItems.addAll(originalList)
        originalItems.sortWith(Comparator { o1, o2 -> o1.id.compareTo(o2.id) })
        submitList(originalList)
    }

    private fun submitList(thingList: MutableList<DeletedThingAdded>) {
        thingDiffCallBack.setItems(filteredItems, thingList)
        val result = DiffUtil.calculateDiff(thingDiffCallBack)
        filteredItems.clear()
        filteredItems.addAll(thingList)
        result.dispatchUpdatesTo(this)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ThingAddedViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val thingImage: ImageView = itemView.image_thing_added
        private val thingTitle: TextView = itemView.title_thing_added
        private val thingDescrp: TextView = itemView.descrp_thing_added
        private val editIcon: ImageView = itemView.btnEdit
        fun bind(
            thingAdded: DeletedThingAdded,
            listener: OnItemClickListener
        ) {
            thingTitle.text = thingAdded.thing
            thingDescrp.text = thingAdded.place
            Glide.with(itemView.context)
                .load(
                    CacheStore.instance(itemView.context.getExternalFilesDir("").toString())
                        ?.getCacheFile(thingAdded.cacheUri)
                )
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(thingImage)
            itemView.setOnClickListener {
                listener.onWholeItemClick(adapterPosition, it)
            }
            itemView.setOnLongClickListener {
                listener.onItemClick(adapterPosition,it)
                return@setOnLongClickListener true
            }
            editIcon.setOnClickListener {
                listener.onItemClick(adapterPosition, it)
            }
        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        clickListener = itemClickListener
    }
}
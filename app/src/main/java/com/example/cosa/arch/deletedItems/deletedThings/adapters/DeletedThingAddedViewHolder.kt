package com.example.cosa.arch.deletedItems.deletedThings.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cosa.R
import com.example.cosa.arch.common.OnItemClickListener
import com.example.cosa.arch.deletedItems.deletedThings.DeletedThingsViewModel
import com.example.cosa.databinding.ItemDeletedThingaddedBinding
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.repository.CacheStore
import kotlinx.android.synthetic.main.item_deleted_thingadded.view.*
import kotlinx.android.synthetic.main.item_thingadded.view.*

class DeletedThingAddedViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemDeletedThingaddedBinding = ItemDeletedThingaddedBinding.bind(itemView)
        fun bind(
            thingAdded: DeletedThingAdded,
            viewModel: DeletedThingsViewModel
        ) {
            binding.deletedThingAdded = thingAdded
            binding.viewModel = viewModel
            binding.editDelButton = binding.root.btn_edit_del
            binding.wholeItemDel = binding.root.cvDelThingAdded

        }

}
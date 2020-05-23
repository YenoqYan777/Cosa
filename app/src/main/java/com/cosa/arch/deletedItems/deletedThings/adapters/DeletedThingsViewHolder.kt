package com.cosa.arch.deletedItems.deletedThings.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cosa.arch.deletedItems.deletedThings.DeletedThingsViewModel
import com.cosa.databinding.ItemDeletedThingsBinding
import com.cosa.models.DeletedThings
import kotlinx.android.synthetic.main.item_deleted_things.view.*

class DeletedThingsViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemDeletedThingsBinding = ItemDeletedThingsBinding.bind(itemView)
        fun bind(
            things: DeletedThings,
            viewModel: DeletedThingsViewModel
        ) {
            binding.deletedThingAdded = things
            binding.viewModel = viewModel
            binding.editDelButton = binding.root.btn_edit_del
            binding.wholeItemDel = binding.root.cvDelThingAdded

        }

}
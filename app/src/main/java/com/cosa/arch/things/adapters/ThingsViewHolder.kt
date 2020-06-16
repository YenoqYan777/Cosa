package com.cosa.arch.things.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cosa.arch.things.ThingsViewModel
import com.cosa.databinding.ItemThingsBinding
import com.cosa.models.Things
import kotlinx.android.synthetic.main.item_things.view.*

class ThingsViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemThingsBinding = ItemThingsBinding.bind(itemView)
    fun bind(
        things: Things,
        viewModel: ThingsViewModel
    ) {
        binding.things = things
        binding.viewModel = viewModel
        binding.editButton = binding.root.btn_edit
        binding.wholeItem = binding.root.cvThingAdded
        binding.imgThingAdded = binding.root.image_thing_added
    }
}
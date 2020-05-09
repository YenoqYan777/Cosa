package com.example.cosa.arch.thingAdded.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cosa.arch.thingAdded.ThingAddedViewModel
import com.example.cosa.databinding.ItemThingaddedBinding
import com.example.cosa.models.ThingAdded
import kotlinx.android.synthetic.main.item_thingadded.view.*

class ThingAddedViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemThingaddedBinding = ItemThingaddedBinding.bind(itemView)
    fun bind(
        thingAdded: ThingAdded,
        viewModel: ThingAddedViewModel
    ) {
        binding.thingAdded = thingAdded
        binding.viewModel = viewModel
        binding.editButton = binding.root.btn_edit
        binding.wholeItem = binding.root.cvThingAdded
        binding.imgThingAdded = binding.root.image_thing_added


    }
}
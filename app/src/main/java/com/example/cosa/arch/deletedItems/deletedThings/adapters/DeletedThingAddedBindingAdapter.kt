package com.example.cosa.arch.deletedItems.deletedThings.adapters

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.cosa.R
import com.example.cosa.arch.deletedItems.deletedThings.DeletedThingsViewModel
import com.example.cosa.arch.thingAdded.ThingAddedViewModel
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.CacheStore

object DeletedThingAddedBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImageDel")
    fun loadImage(view: ImageView, cacheUri: String) {
        Glide.with(view.context)
            .load(
                CacheStore.instance(view.context.getExternalFilesDir("").toString())
                    ?.getCacheFile(cacheUri)
            )
            .error(R.drawable.ic_launcher_background)
            .placeholder(R.drawable.ic_launcher_background)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:viewModelDel", "bind:thingAddedDel"], requireAll = false)
    fun longClick(view: CardView, viewModel: DeletedThingsViewModel, thingAdded: DeletedThingAdded) {
        view.setOnLongClickListener(View.OnLongClickListener {
            viewModel.onItemClicked(view, thingAdded)
            return@OnLongClickListener true
        })
    }


}
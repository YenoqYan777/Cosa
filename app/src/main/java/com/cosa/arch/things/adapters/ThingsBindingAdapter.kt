package com.cosa.arch.things.adapters

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cosa.R
import com.cosa.arch.things.ThingsViewModel
import com.cosa.models.Things
import com.cosa.repository.CacheStore

object ThingsBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
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
    @BindingAdapter(value = ["bind:viewModel", "bind:thingAdded"], requireAll = false)
    fun longClick(view: CardView, viewModel: ThingsViewModel, things: Things) {
        view.setOnLongClickListener(View.OnLongClickListener {
            viewModel.onDotsClicked(view, things)
            return@OnLongClickListener true
        })
    }


}
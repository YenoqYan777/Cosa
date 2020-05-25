package com.cosa.arch.thingsDetails

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cosa.R
import com.cosa.repository.CacheStore

object ThingsDetailBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImageDetail")
    fun loadImageDetail(view: ImageView, cacheUri: String?) {
        cacheUri?.let {
            Glide.with(view.context)
                .load(
                    CacheStore.instance(view.context.getExternalFilesDir("").toString())
                        ?.getCacheFile(it)
                )
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(view)
        }
    }
}
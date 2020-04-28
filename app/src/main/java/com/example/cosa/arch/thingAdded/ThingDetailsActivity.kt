package com.example.cosa.arch.thingAdded

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.cosa.R
import com.example.cosa.databinding.ActivityThingDetailsBinding
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.CacheStore
import kotlinx.android.synthetic.main.activity_thing_details.*

class ThingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThingDetailsBinding
    private lateinit var viewModel: ThingAddedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thing_details)
        initViewModel()
        onBackBtnClick()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ThingAddedViewModel::class.java)
        val thing: ThingAdded = viewModel.getThingForDetail()
        binding.thingNameDetail.text = thing.thing
        binding.placeNameDetail.text = thing.place
        binding.thingImgDetail.setImageBitmap(
            CacheStore.instance(thing.cacheUri)!!
                .getCacheFile(thing.cacheUri)
        )
    }
    private fun onBackBtnClick(){
        binding.btnBackDetail.setOnClickListener {
            onBackPressed()
        }
    }
}

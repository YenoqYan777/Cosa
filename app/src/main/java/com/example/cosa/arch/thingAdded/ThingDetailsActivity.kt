package com.example.cosa.arch.thingAdded

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.cosa.R
import com.example.cosa.databinding.ActivityThingDetailsBinding
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.CacheStore
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.RequestConfiguration


class ThingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThingDetailsBinding
    private lateinit var viewModel: ThingAddedViewModel
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thing_details)
        initViewModel()
        initAd()
        onBackBtnClick()
    }

    private fun initAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }
            }
        }
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

    private fun onBackBtnClick() {
        binding.btnBackDetail.setOnClickListener {

            onBackPressed()
        }
    }
}

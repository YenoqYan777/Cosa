package com.cosa.arch.thingsDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.databinding.FragmentThingsDetailsBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

class ThingsDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentThingsDetailsBinding
    private lateinit var viewModel: ThingsDetailViewModel
    private lateinit var mInterstitialAd: InterstitialAd
    private val args : ThingsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_things_details, container, false
        )
        initViewModel()
        return binding.root
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAd()

    }

    private fun initAd() {
        mInterstitialAd = InterstitialAd(requireActivity())
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
        viewModel = ViewModelProviders.of(this).get(ThingsDetailViewModel::class.java)
        viewModel.getThingDetail(args.thingsId)
        viewModel.things.observe(viewLifecycleOwner, Observer {
            binding.viewModel = viewModel
            binding.thingToShow = it
        })
    }
}

package com.example.cosa.arch.thingDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.cosa.R
import com.example.cosa.arch.base.BaseFragment
import com.example.cosa.arch.base.BaseViewModel
import com.example.cosa.databinding.FragmentThingDetailsBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.coroutines.Delay

class ThingDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentThingDetailsBinding
    private lateinit var viewModel: ThingDetailViewModel
    private lateinit var mInterstitialAd: InterstitialAd
    private val args : ThingDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_thing_details, container, false
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
        viewModel = ViewModelProviders.of(this).get(ThingDetailViewModel::class.java)
        viewModel.getThingDetail(args.thingAddedId)
        viewModel.thingAdded.observe(viewLifecycleOwner, Observer {
            binding.viewModel = viewModel
            binding.thingToShow = it
        })
    }
}

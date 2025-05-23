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
import com.cosa.extension.setToolBarColor
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_main.*

class ThingsDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentThingsDetailsBinding
    private lateinit var viewModel: ThingsDetailViewModel
    private lateinit var mInterstitialAd: InterstitialAd
    private val args: ThingsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setToolBarColor(
            R.color.mainDarkBckg
        )
        requireActivity().bottomNavigationView.visibility = View.GONE
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_things_details, container, false
        )
        initViewModel()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAd()
    }

    private fun initAd() {
        mInterstitialAd = InterstitialAd(requireActivity())
        mInterstitialAd.adUnitId = getString(R.string.thing_added_add_key)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        backPressed()
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }

            }
        }
    }

    private fun backPressed() {
        binding.btnBackDetail.setOnClickListener {
            viewModel.navigate(ThingsDetailsFragmentDirections.actionThingsDetailsFragmentToThingsFragment())
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ThingsDetailViewModel::class.java)
        viewModel.getThingDetail(args.thingsId)
        viewModel.things.observe(viewLifecycleOwner, Observer {
            requireActivity().runOnUiThread {
                binding.thingToShow = it
            }
        })
    }
}

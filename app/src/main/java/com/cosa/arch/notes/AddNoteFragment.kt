package com.cosa.arch.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.databinding.FragmentAddNoteBinding
import com.cosa.extension.hideKeyboard
import com.cosa.extension.setToolBarColor
import com.cosa.models.Notes
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_main.*


class AddNoteFragment : BaseFragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setToolBarColor(
            R.color.mainDarkBckg
        )
        requireActivity().bottomNavigationView.visibility = View.GONE
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_note, container, false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        initToolbar()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
//        viewModel.noteItemCount.observe(viewLifecycleOwner, Observer {
//            if (it % 2 == 0) {
//                initAd()
//            }
//        })
//        viewModel.onNoteAdded()
    }

    private fun initAd() {
        mInterstitialAd = InterstitialAd(requireActivity())
        mInterstitialAd.adUnitId = getString(R.string.note_add_key)
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

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().hideKeyboard()
            viewModel.navigateBack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accept -> {
                onAcceptBtnClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun onAcceptBtnClicked() {
        if (!binding.etNoteContent.text.isNullOrEmpty() && binding.etNoteContent.text.toString()
                .trim().isNotEmpty()
        ) {
            if (!binding.etNoteTitle.text.isNullOrEmpty() && binding.etNoteTitle.text.toString()
                    .trim().isNotEmpty()
            ) {
                viewModel.insertNote(
                    Notes(
                        text = binding.etNoteContent.text.toString(),
                        title = binding.etNoteTitle.text.toString()
                    )
                )
            } else {
                val arr: List<String> = binding.etNoteContent.text.toString().split(" ")
                val title = if (arr.size > 1) {
                    "${arr[0]} ${arr[1]}"
                } else {
                    arr[0]
                }
                viewModel.insertNote(
                    Notes(
                        text = binding.etNoteContent.text.toString(),
                        title = title
                    )
                )
            }
        }
        requireActivity().hideKeyboard()
        viewModel.navigateBack()
    }
}

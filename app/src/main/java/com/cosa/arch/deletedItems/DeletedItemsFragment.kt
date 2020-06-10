package com.cosa.arch.deletedItems

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cosa.R
import com.cosa.arch.deletedItems.adapters.PagerAdapter
import com.cosa.databinding.FragmentDeletedItemsBinding
import com.cosa.extension.setToolBarColor
import kotlinx.android.synthetic.main.activity_main.*


class DeletedItemsFragment : Fragment(){
    private lateinit var binding: FragmentDeletedItemsBinding
    private lateinit var adapter: PagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setToolBarColor(R.color.darkerBckg)
        requireActivity().bottomNavigationView.visibility = View.VISIBLE
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deleted_items, container, false)
        adapter = PagerAdapter(this.childFragmentManager, requireActivity())
        binding.viewPagerDeletedItems.adapter = adapter
        binding.tabLayoutDeletedItems.setupWithViewPager(binding.viewPagerDeletedItems)
        return binding.root
    }



}

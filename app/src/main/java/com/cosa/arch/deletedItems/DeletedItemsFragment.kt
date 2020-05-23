package com.cosa.arch.deletedItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cosa.R
import com.cosa.arch.deletedItems.adapters.PagerAdapter
import com.cosa.databinding.FragmentDeletedItemsBinding


class DeletedItemsFragment : Fragment(){
    private lateinit var binding: FragmentDeletedItemsBinding
    private lateinit var adapter: PagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deleted_items, container, false)
        adapter = PagerAdapter(this.childFragmentManager, activity!!)
        binding.viewPagerDeletedItems.adapter = adapter
        binding.tabLayoutDeletedItems.setupWithViewPager(binding.viewPagerDeletedItems)
        return binding.root
    }
}

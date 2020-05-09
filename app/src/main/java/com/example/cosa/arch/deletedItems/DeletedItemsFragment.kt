package com.example.cosa.arch.deletedItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cosa.R
import com.example.cosa.arch.MainActivity
import com.example.cosa.arch.base.BaseFragment
import com.example.cosa.arch.deletedItems.adapters.PagerAdapter
import com.example.cosa.databinding.FragmentDeletedItemsBinding
import kotlinx.android.synthetic.main.fragment_deleted_items.*


class DeletedItemsFragment : BaseFragment(){
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

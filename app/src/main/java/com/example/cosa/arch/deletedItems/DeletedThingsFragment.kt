package com.example.cosa.arch.deletedItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cosa.R
import com.example.cosa.databinding.FragmentDeletedItemsBinding

class DeletedThingsFragment : Fragment() {
    private lateinit var binding: FragmentDeletedItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deleted_items, container, false)



        return binding.root

    }

}

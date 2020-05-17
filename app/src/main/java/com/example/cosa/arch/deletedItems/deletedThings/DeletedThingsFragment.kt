package com.example.cosa.arch.deletedItems.deletedThings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosa.R
import com.example.cosa.arch.common.WrapContentLinearLayoutManager
import com.example.cosa.arch.deletedItems.deletedThings.adapters.DeletedThingAddedAdapter
import com.example.cosa.arch.deletedItems.deletedThings.adapters.DeletedThingDiffCallBack
import com.example.cosa.databinding.FragmentDeletedThingsBinding
import com.example.cosa.models.DeletedThingAdded
import kotlinx.android.synthetic.main.fragment_deleted_things.*

class DeletedThingsFragment : Fragment() {
    private lateinit var binding: FragmentDeletedThingsBinding
    private lateinit var viewModel: DeletedThingsViewModel
    private lateinit var adapter: DeletedThingAddedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deleted_things, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerView()
        addSearchViewTextChanges()
        observe()
    }

    private fun observe() {
        viewModel.itemClickedDelThing.observe(viewLifecycleOwner, Observer {
            createMenuForRecyclerView(it.first, it.second)
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DeletedThingsViewModel::class.java)

        viewModel.getDeletedThingAdded().observe(viewLifecycleOwner, Observer {
            activity?.runOnUiThread {
                adapter.setOriginalItems(it)
            }
            if (it.isEmpty()) {
                noItemDeleted.visibility = View.VISIBLE
                imgNoThingsDeleted.visibility = View.VISIBLE
            } else {
                noItemDeleted.visibility = View.GONE
                imgNoThingsDeleted.visibility = View.GONE
            }
        })
    }

    private fun initRecyclerView() {
        val mLayoutManager = WrapContentLinearLayoutManager(requireContext())
        adapter =
            DeletedThingAddedAdapter(
                DeletedThingDiffCallBack(),
                requireContext(),
                viewModel
            )
        rvDeletedThings.layoutManager = mLayoutManager
        rvDeletedThings.adapter = adapter
    }

    private fun createMenuForRecyclerView(view: View, deletedThingAdded: DeletedThingAdded) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.deleted_itme_edit_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.recovery -> {
                    viewModel.recoverItem(deletedThingAdded)
                    adapter.notifyDataSetChanged()
                }
                R.id.delete -> {
                    viewModel.completelyDeleteThing(deletedThingAdded)
                }
            }
            false
        }
        popup.show()
    }

    private fun addSearchViewTextChanges() {
        binding.svDeletedThings.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }


}

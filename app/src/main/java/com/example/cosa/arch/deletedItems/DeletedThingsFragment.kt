package com.example.cosa.arch.deletedItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosa.R
import com.example.cosa.arch.deletedItems.adapters.DeletedThingAddedAdapter
import com.example.cosa.arch.deletedItems.adapters.DeletedThingDiffCallBack
import com.example.cosa.arch.helpers.OnItemClickListener
import com.example.cosa.arch.helpers.SwipeHandler
import com.example.cosa.arch.thingAdded.ThingAddedViewModel
import com.example.cosa.databinding.FragmentDeletedThingsBinding
import kotlinx.android.synthetic.main.fragment_deleted_things.*

class DeletedThingsFragment : Fragment(), SwipeHandler {
    private lateinit var binding: FragmentDeletedThingsBinding
    private lateinit var viewModel: ThingAddedViewModel
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
        recyclerItemClickListener()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ThingAddedViewModel::class.java)

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
        val mLayoutManager = LinearLayoutManager(activity)
        adapter = DeletedThingAddedAdapter(DeletedThingDiffCallBack(), context!!)
        rvDeletedThings.layoutManager = mLayoutManager
        rvDeletedThings.adapter = adapter
    }
    private fun recyclerItemClickListener() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                createMenuForRecyclerView(position, view)
            }

            override fun onWholeItemClick(position: Int, view: View) {
                createMenuForRecyclerView(position, view)
            }

        })
    }
    private fun createMenuForRecyclerView(position: Int, view: View) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.deleted_itme_edit_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    viewModel.completelyDeleteThing(adapter.getData()[position])
                }
            }
            false
        }
        popup.show()
    }

    override fun onItemSwipedRight(position: Int) {
        viewModel.completelyDeleteThing(adapter.getData()[position])
    }

    override fun onItemSwipedLeft(position: Int) {
        adapter.notifyItemChanged(position)
    }


}

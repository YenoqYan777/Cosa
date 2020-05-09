package com.example.cosa.arch.deletedItems.deletedNotes

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
import com.example.cosa.R
import com.example.cosa.arch.deletedItems.deletedNotes.adapters.DeletedNotesAdapter
import com.example.cosa.arch.deletedItems.deletedNotes.adapters.DeletedNotesDiffCallback
import com.example.cosa.arch.common.OnItemClickListener
import com.example.cosa.arch.common.WrapContentLinearLayoutManager
import com.example.cosa.databinding.FragmentDeletedNotesBinding
import com.example.cosa.models.DeletedNotes
import com.example.cosa.models.Notes
import kotlinx.android.synthetic.main.fragment_deleted_notes.*

class DeletedNotesFragment : Fragment() {
    private lateinit var binding: FragmentDeletedNotesBinding
    private lateinit var viewModel: DeletedNotesViewModel

    private lateinit var adapter: DeletedNotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deleted_notes, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecycler()
        performSearch()
        observe()
    }

    private fun observe() {
        viewModel.itemClicked.observe(viewLifecycleOwner, Observer {
            createMenuForRecyclerView(it.first, it.second)
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DeletedNotesViewModel::class.java)
        viewModel.getDeletedData().observe(viewLifecycleOwner, Observer {
            activity!!.runOnUiThread {
                adapter.setOriginalItems(it)
            }

            if (it.isEmpty()) {
                noNotesDeleted.visibility = View.VISIBLE
                imgNoNotesDeleted.visibility = View.VISIBLE
            } else {
                noNotesDeleted.visibility = View.GONE
                imgNoNotesDeleted.visibility = View.GONE
            }
        })
    }

    private fun initRecycler() {
        val mLayoutManager = WrapContentLinearLayoutManager(activity!!)
        adapter =
            DeletedNotesAdapter(
                DeletedNotesDiffCallback(),
                context!!,
                viewModel
            )
        rvDeletedNotes.layoutManager = mLayoutManager
        rvDeletedNotes.adapter = adapter
    }

    private fun createMenuForRecyclerView(view: View, delNotes: DeletedNotes) {
        val delNote = Notes()
        delNote.id = delNotes.id
        delNote.text = delNotes.text


        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.deleted_itme_edit_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.recovery -> {
                    viewModel.recoverNote(delNote)
                    adapter.notifyDataSetChanged()
                }
                R.id.delete -> {
                    viewModel.completelyDeleteNote(delNotes)
                }
            }
            false
        }
        popup.show()
    }

    private fun performSearch() {
        binding.svDeletedNotes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

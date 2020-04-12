package com.example.cosa.arch.notes

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
import com.example.cosa.arch.helpers.OnItemClickListener
import com.example.cosa.arch.helpers.SwipeHandler
import com.example.cosa.arch.notes.adapters.NotesAdapter
import com.example.cosa.arch.notes.adapters.NotesDiffCallback
import com.example.cosa.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment(), SwipeHandler {
    private lateinit var binding: FragmentNotesBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter
    private val addNotesFragment: AddNoteFragment =
        AddNoteFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeHandler = this
        initViewModel()
        initRecyclerView()
        recyclerItemClickListener()
        onAddBtnClick()
        addSearchViewTextChanges()
        recyclerItemClickListener()
    }

    private fun addSearchViewTextChanges() {
        searchViewNotes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun onAddBtnClick() {
        binding.btnAddNote.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, addNotesFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        viewModel.getNotes().observe(viewLifecycleOwner, Observer {
            activity?.runOnUiThread {
                adapter.setOriginalItems(it)
            }
            if (it.isEmpty()) {
                txtNoNotes.visibility = View.VISIBLE
                imgNoNotes.visibility = View.VISIBLE
            } else {
                txtNoNotes.visibility = View.GONE
                imgNoNotes.visibility = View.GONE
            }
        })
    }

    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(activity)
        adapter = NotesAdapter(NotesDiffCallback(), context!!)
        rvNoteList.layoutManager = mLayoutManager
        rvNoteList.adapter = adapter
    }

    private fun recyclerItemClickListener() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                createMenuForRecyclerView(position, view)
            }
        })
    }

    private fun createMenuForRecyclerView(position: Int, view: View) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.itme_edit_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    onEditBtnClick(position)
                }
                R.id.delete -> {
                    itemDelete(position)
                }
            }
            false
        }
        popup.show()
    }

    private fun itemDelete(position: Int) {

    }

    private fun onEditBtnClick(position: Int) {

    }

    override fun onItemSwipedRight(position: Int) {

    }

    override fun onItemSwipedLeft(position: Int) {
    }


}

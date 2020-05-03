package com.example.cosa.arch.notes

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cosa.R
import com.example.cosa.arch.helpers.OnItemClickListener
import com.example.cosa.arch.helpers.SwipeHandler
import com.example.cosa.arch.helpers.WrapContentLinearLayoutManager
import com.example.cosa.arch.notes.adapters.NotesAdapter
import com.example.cosa.arch.notes.adapters.NotesDiffCallback
import com.example.cosa.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment(), SwipeHandler {
    private val SAVETRASH: String = "SAVETRASH"
    private val SHARED: String = "sharedPref"

    private lateinit var binding: FragmentNotesBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
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
        performSearch()
        recyclerItemClickListener()
    }

    private fun performSearch() {
        searchViewNotes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.filter.filter(newText)
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
                notesAdapter.setOriginalItems(it)
            }
            if (it.isEmpty()) {
                txtNoNotes.visibility = VISIBLE
                imgNoNotes.visibility = VISIBLE
                txtIntroductionNotes.visibility = GONE
            } else {
                txtNoNotes.visibility = GONE
                imgNoNotes.visibility = GONE
                txtIntroductionNotes.visibility = VISIBLE
            }
        })
    }

    private fun initRecyclerView() {
        val mLayoutManager = WrapContentLinearLayoutManager(context!!)
        notesAdapter = NotesAdapter(NotesDiffCallback(), context!!)
        rvNoteList.apply {
            adapter = notesAdapter
            layoutManager = mLayoutManager
        }
    }

    private fun recyclerItemClickListener() {
        notesAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                createMenuForRecyclerView(position, view)
            }

            override fun onWholeItemClick(position: Int, view: View) {

            }
        })
    }

    private fun createMenuForRecyclerView(position: Int, view: View) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.itme_edit_view_menu)
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

    private fun onEditBtnClick(position: Int) {
        viewModel.setEditTextMessage(notesAdapter.getData()[position].text)
        viewModel.setItemId(notesAdapter.getData()[position].id)
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, EditNoteFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun itemDelete(position: Int) {
        val dialogClickListener: DialogInterface.OnClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val pref: SharedPreferences =
                            activity!!.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
                        viewModel.deleteItem(notesAdapter.getData()[position], pref.getBoolean(SAVETRASH, true))
                    }
                }
            }

        val builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.are_you_sure))
            .setOnDismissListener {
                notesAdapter.notifyItemChanged(position)
            }
            .setPositiveButton(getString(R.string.yes), dialogClickListener)
            .setNegativeButton(getString(R.string.no), dialogClickListener)
            .show()
    }


    override fun onItemSwipedRight(position: Int) {
        itemDelete(position)
    }

    override fun onItemSwipedLeft(position: Int) {
        onEditBtnClick(position)
        notesAdapter.notifyItemChanged(position)
    }


}


package com.cosa.arch.notes

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.arch.common.SwipeHandler
import com.cosa.arch.common.WrapContentLinearLayoutManager
import com.cosa.arch.notes.adapters.NotesAdapter
import com.cosa.arch.notes.adapters.NotesDiffCallback
import com.cosa.databinding.FragmentNotesBinding
import com.cosa.extension.setToolBarColor
import com.cosa.helper.LocalManager.SAVE_TRASH_KEY_NOTES
import com.cosa.helper.LocalManager.SHARED
import com.cosa.models.Notes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_are_you_sure.view.*
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : BaseFragment(), SwipeHandler {
    private lateinit var binding: FragmentNotesBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setToolBarColor(
            requireActivity(),
            requireActivity(),
            R.color.mainDarkBckg
        )
        requireActivity().bottomNavigationView.visibility = VISIBLE
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes, container, false
        )

        return binding.root
    }

    override fun getViewModel(): BaseViewModel = viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeHandler = this
        initViewModel()
        initRecyclerView()
        onAddBtnClick()
        performSearch()
        observe()
    }

    private fun observe() {
        viewModel.itemClicked.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { pair ->
                createMenuForRecyclerView(pair.first, pair.second)
            }
        })
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
            viewModel.navigate(NotesFragmentDirections.actionNotesFragmentToAddNoteFragment())
        }
    }

    private fun initRecyclerView() {
        val mLayoutManager = WrapContentLinearLayoutManager(requireContext())
        notesAdapter = NotesAdapter(NotesDiffCallback(), requireContext(), viewModel)
        rvNoteList.apply {
            adapter = notesAdapter
            layoutManager = mLayoutManager
        }
    }

    private fun createMenuForRecyclerView(view: View, notes: Notes) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.itme_edit_view_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    onEditBtnClick(notes)
                }
                R.id.delete -> {
                    itemDelete(notes)
                }
            }
            false
        }
        popup.show()
    }

    private fun onEditBtnClick(notes: Notes) {
        viewModel.setEditTextMessage(notes.text)
        viewModel.setItemId(notes.id)
        viewModel.navigate(NotesFragmentDirections.actionNotesFragmentToEditNoteFragment())
    }


    private fun itemDelete(notes: Notes) {
        val dialogViewDelItem =
            View.inflate(requireContext(), R.layout.dialog_are_you_sure, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogViewDelItem)
        val myAlarmDialog = mBuilder.show()

        myAlarmDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogViewDelItem.choice_yes.setOnClickListener {
            val pref: SharedPreferences =
                requireActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE)
            viewModel.deleteItem(
                notes, pref.getBoolean(
                    SAVE_TRASH_KEY_NOTES, true
                )
            )
            myAlarmDialog.dismiss()
        }

        dialogViewDelItem.choice_no.setOnClickListener {
            myAlarmDialog.dismiss()
            notesAdapter.notifyDataSetChanged()
        }

    }

    override fun onItemSwipedRight(position: Int) {
        itemDelete(notesAdapter.getData()[position])
    }

    override fun onItemSwipedLeft(position: Int) {
        onEditBtnClick(notesAdapter.getData()[position])
        notesAdapter.notifyItemChanged(position)
    }
}


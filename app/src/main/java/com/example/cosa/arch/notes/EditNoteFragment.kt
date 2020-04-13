package com.example.cosa.arch.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cosa.R
import com.example.cosa.databinding.FragmentEditNoteBinding

class EditNoteFragment : Fragment() {
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_note, container, false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarEditNote)
        initToolbar()
        initViewModel()
        binding.etEditNoteContent.setText(viewModel.getEditTextMessage())
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title =
            getString(R.string.edit_your_note);
        binding.toolbarEditNote.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarEditNote.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accept -> {
                onAcceptBtnClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun onAcceptBtnClicked() {
        if (!binding.etEditNoteContent.text.isNullOrEmpty()) {
            viewModel.updateNoteInfo(
                binding.etEditNoteContent.text.toString(),
                viewModel.getItemId()
            )
            fragmentManager!!.beginTransaction()
                .replace(R.id.fragment, NotesFragment())
                .commit()

        } else {
            fragmentManager!!.beginTransaction()
                .replace(R.id.fragment, NotesFragment())
                .commit()
        }
    }
}

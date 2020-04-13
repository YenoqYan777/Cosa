package com.example.cosa.arch.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cosa.R
import com.example.cosa.databinding.FragmentAddNoteBinding
import com.example.cosa.models.Notes


class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_note, container, false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        initToolbar()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = "Create new note";
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
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
        if (!binding.etNoteContent.text.isNullOrEmpty()) {
            viewModel.insertNote(Notes(text = binding.etNoteContent.text.toString()))
            val transaction = fragmentManager
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

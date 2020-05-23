package com.cosa.arch.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.databinding.FragmentAddNoteBinding
import com.cosa.extension.hideKeyboard
import com.cosa.models.Notes


class AddNoteFragment : BaseFragment() {
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
        binding.etNoteContent.setText("")
        initToolbar()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            hideKeyboard(requireActivity())
            viewModel.navigateBack()
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
            hideKeyboard(requireActivity())
            viewModel.navigateBack()
        } else {
            hideKeyboard(requireActivity())
            viewModel.navigateBack()
        }
    }


}

package com.cosa.arch.notes

import android.content.res.TypedArray
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.databinding.FragmentEditNoteBinding
import com.cosa.extension.hideKeyboard


class EditNoteFragment : BaseFragment() {
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var navController: NavController
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

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true);
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false);
        val a: TypedArray =
            requireActivity().theme.obtainStyledAttributes(R.style.AppTheme, intArrayOf(R.attr.backIcon))
        val attributeResourceId = a.getResourceId(0, 0)
        val drawable = resources.getDrawable(attributeResourceId)
        binding.toolbarEditNote.navigationIcon = drawable
        binding.toolbarEditNote.setNavigationOnClickListener {
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
        if (!binding.etEditNoteContent.text.isNullOrEmpty()) {
            viewModel.updateNoteInfo(
                binding.etEditNoteContent.text.toString(),
                viewModel.getItemId()
            )
        }
        hideKeyboard(requireActivity())
        viewModel.navigateBack()
    }
}

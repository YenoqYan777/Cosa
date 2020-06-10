package com.cosa.arch.notes

import android.content.res.TypedArray
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.databinding.FragmentEditNoteBinding
import com.cosa.extension.hideKeyboard
import com.cosa.extension.setToolBarColor
import kotlinx.android.synthetic.main.activity_main.*


class EditNoteFragment : BaseFragment() {
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setToolBarColor(
            R.color.mainDarkBckg
        )
        requireActivity().bottomNavigationView.visibility = View.GONE
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_note, container, false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarEditNote)
        initToolbar()
        initViewModel()
        //TODO do with data binding
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
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        val a: TypedArray =
            requireActivity().theme.obtainStyledAttributes(
                R.style.AppTheme,
                intArrayOf(R.attr.backIcon)
            )
        val attributeResourceId = a.getResourceId(0, 0)
        val drawable = resources.getDrawable(attributeResourceId)
        binding.toolbarEditNote.navigationIcon = drawable
        binding.toolbarEditNote.setNavigationOnClickListener {
            requireActivity().hideKeyboard()
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
        if (!binding.etEditNoteContent.text.isNullOrEmpty() && binding.etEditNoteContent.text.toString()
                .trim().isNotEmpty()
        ) {
            viewModel.updateNoteInfo(
                binding.etEditNoteContent.text.toString(),
                viewModel.getItemId()
            )
        }
        requireActivity().hideKeyboard()
        viewModel.navigateBack()
    }
}

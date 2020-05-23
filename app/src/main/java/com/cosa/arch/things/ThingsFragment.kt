package com.cosa.arch.things

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cosa.R
import com.cosa.arch.base.BaseFragment
import com.cosa.arch.base.BaseViewModel
import com.cosa.arch.common.SwipeHandler
import com.cosa.arch.things.adapters.ThingsAdapter
import com.cosa.arch.things.adapters.ThingsDiffCallBack
import com.cosa.databinding.FragmentThingsBinding
import com.cosa.models.Things
import com.cosa.repository.CacheStore
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.add_item_dialog.view.*
import kotlinx.android.synthetic.main.dialog_are_you_sure.view.*
import kotlinx.android.synthetic.main.fragment_things.*
import kotlinx.android.synthetic.main.item_edit_fragment_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ThingsFragment : BaseFragment(), SwipeHandler {
    private val SAVETRASH: String = "SAVETRASH"
    private val SHARED: String = "sharedPref"

    private lateinit var binding: FragmentThingsBinding
    private lateinit var viewModel: ThingsViewModel
    private lateinit var thingsAdapter: ThingsAdapter
    private lateinit var imgThingUploaded: ImageView
    private lateinit var imgEditThing: ImageView
    private var things: Things = Things()

    private var isImageUploaded: Boolean = false

    companion object {
        private const val CAMERA_RC = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_things, container, false
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
        viewModel.dotClicked.observe(viewLifecycleOwner, Observer {
            createMenuForRecyclerView(it.first, it.second)
        })

        viewModel.wholeClicked.observe(viewLifecycleOwner, Observer {
            viewModel.navigate(
                ThingsFragmentDirections.actionThingsFragmentToThingsDetailFragment(
                    it.id
                )
            )
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ThingsViewModel::class.java)
        viewModel.getThingAdded().observe(viewLifecycleOwner, Observer {
            activity?.runOnUiThread {
                thingsAdapter.setOriginalItems(it)
            }
            if (it.isEmpty()) {
                noItemYetText.visibility = VISIBLE
                imgNoThings.visibility = VISIBLE
                txtIntroductionThingAdded.visibility = GONE
            } else {
                noItemYetText.visibility = GONE
                imgNoThings.visibility = GONE
                txtIntroductionThingAdded.visibility = VISIBLE
            }
        })
    }

    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(activity)
        thingsAdapter = ThingsAdapter(ThingsDiffCallBack(), requireContext(), viewModel)
        rv_thing_list.layoutManager = mLayoutManager
        rv_thing_list.adapter = thingsAdapter
    }

    private fun createMenuForRecyclerView(view: View, things: Things) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.itme_edit_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    onEditBtnClick(things)
                }
                R.id.delete -> {
                    itemDelete(things)
                }
            }
            false
        }
        popup.show()
    }

    private fun performSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                thingsAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun onEditBtnClick(mThings: Things) {
        val dialogViewEditItem =
            LayoutInflater.from(activity).inflate(R.layout.item_edit_fragment_dialog, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogViewEditItem)

        if (mThings.cacheUri.isNotEmpty()) {
            things.cacheUri = mThings.cacheUri
            dialogViewEditItem.imgEditItem.setImageBitmap(
                CacheStore.instance(mThings.cacheUri)!!
                    .getCacheFile(mThings.cacheUri)
            )
        } else {
            dialogViewEditItem.imgEditItem.setImageResource(R.drawable.ic_take_photo)
        }

        dialogViewEditItem.editNameText.setText(mThings.thing)
        dialogViewEditItem.editPlaceText.setText(mThings.place)

        val myAlarmDialog = mBuilder.show()

        myAlarmDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        imgEditThing = dialogViewEditItem.imgEditItem

        dialogViewEditItem.imgEditItem.setOnClickListener {
            takePhotoBtnClick()
        }
        dialogViewEditItem.btnSaveChanges.setOnClickListener {
            saveChangesBtnClick(dialogViewEditItem, myAlarmDialog, mThings)
        }
        dialogViewEditItem.imgCloseEditDialog.setOnClickListener {
            myAlarmDialog.dismiss()
        }
    }

    private fun saveChangesBtnClick(
        dialogView: View,
        myAlarmDialog: AlertDialog,
        mThings: Things
    ) {
        when {
            dialogView.editNameText.text.toString()
                .isEmpty() &&
                    dialogView.editPlaceText.text.toString()
                        .isEmpty() -> {
                setError(dialogView.editPlaceInputLayout, R.string.place_required_error)
                setError(dialogView.editNameInputLayout, R.string.name_required_error)
            }
            dialogView.editNameText.text.toString()
                .isEmpty() -> {
                setError(dialogView.editPlaceInputLayout, null)
                setError(dialogView.editNameInputLayout, R.string.name_required_error)
            }
            dialogView.editPlaceText.text.toString()
                .isEmpty() -> {
                setError(dialogView.editPlaceInputLayout, R.string.place_required_error)
                setError(dialogView.editNameInputLayout, null)
            }
            else -> {
                val thingName = dialogView.editNameText.text.toString()
                val thingPlace = dialogView.editPlaceText.text.toString()
                val cacheUri = if (things.cacheUri.isNotBlank()) {
                    things.cacheUri
                } else {
                    mThings.cacheUri
                }
                val id = mThings.id
                setError(dialogView.editPlaceInputLayout, null)
                setError(dialogView.editNameInputLayout, null)
                viewModel.updateThingInfo(thingName, thingPlace, cacheUri, id)
                myAlarmDialog.dismiss()
            }
        }
    }

    private fun onAddBtnClick() {
        btnAddNote.setOnClickListener {
            isImageUploaded = false
            things.cacheUri = ""
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.add_item_dialog, null)
            val mBuilder = AlertDialog.Builder(activity)
                .setView(dialogView)

            val myAlarmDialog = mBuilder.show()
            myAlarmDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            imgThingUploaded = dialogView.imgUploadedPhoto
            dialogView.imgUploadedPhoto.setOnClickListener {
                takePhotoBtnClick()
            }
            dialogView.btnAddNote.setOnClickListener {
                acceptBtnClick(dialogView, myAlarmDialog)
            }
            dialogView.btnCloseAddItemDialog.setOnClickListener {
                myAlarmDialog.dismiss()
            }
        }
    }

    private fun acceptBtnClick(dialogView: View, myAlarmDialog: AlertDialog) {

        when {
            !isImageUploaded &&
                    dialogView.nameText.text.toString()
                        .isEmpty() &&
                    dialogView.placeText.text.toString()
                        .isEmpty() -> {
                setError(dialogView.placeInputLayout, R.string.place_required_error)
                setError(dialogView.nameInputLayout, R.string.name_required_error)
                Toast.makeText(activity, getString(R.string.take_a_photo), Toast.LENGTH_LONG).show()
            }
            !isImageUploaded &&
                    dialogView.nameText.text.toString()
                        .isEmpty() -> {
                Toast.makeText(activity, getString(R.string.take_a_photo), Toast.LENGTH_LONG).show()
                setError(dialogView.placeInputLayout, null)
                setError(dialogView.nameInputLayout, R.string.name_required_error)
            }
            !isImageUploaded &&
                    dialogView.placeText.text.toString()
                        .isEmpty() -> {
                setError(dialogView.placeInputLayout, R.string.place_required_error)
                setError(dialogView.nameInputLayout, null)
                Toast.makeText(activity, getString(R.string.take_a_photo), Toast.LENGTH_LONG).show()
            }
            !isImageUploaded -> {
                Toast.makeText(activity, getString(R.string.take_a_photo), Toast.LENGTH_LONG).show()
            }
            dialogView.nameText.text.toString()
                .isEmpty() &&
                    dialogView.placeText.text.toString()
                        .isEmpty() -> {
                setError(dialogView.placeInputLayout, R.string.place_required_error)
                setError(dialogView.nameInputLayout, R.string.name_required_error)
            }
            dialogView.nameText.text.toString()
                .isEmpty() -> {
                setError(dialogView.placeInputLayout, null)
                setError(dialogView.nameInputLayout, R.string.name_required_error)
            }
            dialogView.placeText.text.toString()
                .isEmpty() -> {
                setError(dialogView.placeInputLayout, R.string.place_required_error)
                setError(dialogView.nameInputLayout, null)
            }
            else -> {
                setError(dialogView.placeInputLayout, null)
                setError(dialogView.nameInputLayout, null)
                myAlarmDialog.dismiss()
                things.place = dialogView.placeText.text.toString()
                things.thing = dialogView.nameText.text.toString()
                viewModel.insertThingAdded(things)
                rv_thing_list.smoothScrollToPosition(thingsAdapter.itemCount);
            }
        }
    }

    private fun takePhotoBtnClick() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_RC)
    }

    private fun setError(textInputLayout: TextInputLayout, error: Int?) {
        if (error == null) {
            textInputLayout.error = error
        } else {
            textInputLayout.error = getString(error)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_RC && resultCode == Activity.RESULT_OK) {
            val generatedCacheUri = SimpleDateFormat("ddMMyyhhmmssSSS").format(Date())
            val photo = data!!.extras!!["data"] as Bitmap?

            if (::imgThingUploaded.isInitialized) {
                imgThingUploaded.setImageBitmap(photo)
                isImageUploaded = true
            }

            if (::imgEditThing.isInitialized) {
                imgEditThing.setImageBitmap(photo)
            }

            CacheStore.instance(context?.getExternalFilesDir("").toString())!!
                .saveCacheFile(generatedCacheUri, photo!!)
            things.cacheUri = generatedCacheUri
        }
    }

    private fun itemDelete(mThings: Things) {
        val dialogViewDelItem =
            LayoutInflater.from(activity).inflate(R.layout.dialog_are_you_sure, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogViewDelItem)
        val myAlarmDialog = mBuilder.show()

        myAlarmDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogViewDelItem.choice_yes.setOnClickListener {
            val pref: SharedPreferences =
                requireActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE)
            viewModel.deleteItem(
                mThings,
                pref.getBoolean(SAVETRASH, true)
            )
            myAlarmDialog.dismiss()
        }

        dialogViewDelItem.choice_no.setOnClickListener {
            myAlarmDialog.dismiss()
            thingsAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemSwipedRight(position: Int) {
        itemDelete(thingsAdapter.getData()[position])
    }

    override fun onItemSwipedLeft(position: Int) {
        onEditBtnClick(thingsAdapter.getData()[position])
        thingsAdapter.notifyItemChanged(position)
    }
}
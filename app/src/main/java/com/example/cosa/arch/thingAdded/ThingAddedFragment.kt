package com.example.cosa.arch.thingAdded

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import com.example.cosa.R
import com.example.cosa.arch.base.BaseFragment
import com.example.cosa.arch.common.SwipeHandler
import com.example.cosa.arch.thingAdded.adapters.ThingAddedAdapter
import com.example.cosa.arch.thingAdded.adapters.ThingDiffCallBack
import com.example.cosa.databinding.FragmentThingListBinding
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.CacheStore
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.add_item_dialog.view.*
import kotlinx.android.synthetic.main.fragment_thing_list.*
import kotlinx.android.synthetic.main.item_edit_fragment_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ThingAddedFragment : BaseFragment(), SwipeHandler {
    private val SAVETRASH: String = "SAVETRASH"
    private val SHARED: String = "sharedPref"

    private lateinit var binding: FragmentThingListBinding
    private lateinit var viewModel: ThingAddedViewModel
    private lateinit var thingAddedAdapter: ThingAddedAdapter
    private lateinit var imgThingUploaded: ImageView
    private lateinit var imgEditThing: ImageView
    private var thingAdded: ThingAdded = ThingAdded()
    private var isImageUploaded: Boolean = false

    companion object {
        private const val CAMERA_RC = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_thing_list, container, false
        )

        return binding.root
    }

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
            viewModel.setThingForThingAdded(it)
            context!!.startActivity(Intent(context, ThingDetailsActivity::class.java))
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ThingAddedViewModel::class.java)
        viewModel.getThingAdded().observe(viewLifecycleOwner, Observer {
            activity?.runOnUiThread {
                thingAddedAdapter.setOriginalItems(it)
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
        thingAddedAdapter = ThingAddedAdapter(ThingDiffCallBack(), context!!, viewModel)
        rv_thing_list.layoutManager = mLayoutManager
        rv_thing_list.adapter = thingAddedAdapter
    }

    private fun createMenuForRecyclerView(view: View, thingAdded: ThingAdded) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.itme_edit_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    onEditBtnClick(thingAdded)
                }
                R.id.delete -> {
                    itemDelete(thingAdded)
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
                thingAddedAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun onEditBtnClick(mThingAdded: ThingAdded) {
        val dialogViewEditItem =
            LayoutInflater.from(activity).inflate(R.layout.item_edit_fragment_dialog, null)

        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogViewEditItem)

        if (mThingAdded.cacheUri.isNotEmpty()) {
            thingAdded.cacheUri = mThingAdded.cacheUri
            dialogViewEditItem.imgEditItem.setImageBitmap(
                CacheStore.instance(mThingAdded.cacheUri)!!
                    .getCacheFile(mThingAdded.cacheUri)
            )
        } else {
            dialogViewEditItem.imgEditItem.setImageResource(R.drawable.ic_take_photo)
        }

        dialogViewEditItem.editNameText.setText(mThingAdded.thing)
        dialogViewEditItem.editPlaceText.setText(mThingAdded.place)

        val myAlarmDialog = mBuilder.show()

        myAlarmDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        imgEditThing = dialogViewEditItem.imgEditItem

        dialogViewEditItem.imgEditItem.setOnClickListener {
            takePhotoBtnClick()
        }
        dialogViewEditItem.btnSaveChanges.setOnClickListener {
            saveChangesBtnClick(dialogViewEditItem, myAlarmDialog, mThingAdded)
        }
        dialogViewEditItem.imgCloseEditDialog.setOnClickListener {
            myAlarmDialog.dismiss()
        }
    }

    private fun saveChangesBtnClick(
        dialogView: View,
        myAlarmDialog: AlertDialog,
        mThingAdded: ThingAdded
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
                val cacheUri = if (thingAdded.cacheUri.isNotBlank()) {
                    thingAdded.cacheUri
                } else {
                    mThingAdded.cacheUri
                }
                val id = mThingAdded.id
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
            thingAdded.cacheUri = ""
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
                thingAdded.place = dialogView.placeText.text.toString()
                thingAdded.thing = dialogView.nameText.text.toString()
                viewModel.insertThingAdded(thingAdded)
                rv_thing_list.smoothScrollToPosition(thingAddedAdapter.itemCount);
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
            thingAdded.cacheUri = generatedCacheUri
        }
    }

    private fun itemDelete(mThingAdded: ThingAdded) {
        val dialogClickListener: DialogInterface.OnClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val pref: SharedPreferences =
                            activity!!.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
                        viewModel.deleteItem(
                            mThingAdded,
                            pref.getBoolean(SAVETRASH, true)
                        )
                    }
                }
            }

        val builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.are_you_sure))
            .setOnDismissListener {
                thingAddedAdapter.notifyDataSetChanged()
            }
            .setPositiveButton(getString(R.string.yes), dialogClickListener)
            .setNegativeButton(getString(R.string.no), dialogClickListener)
            .show()
    }

    override fun onItemSwipedRight(position: Int) {
        itemDelete(thingAddedAdapter.getData()[position])
    }

    override fun onItemSwipedLeft(position: Int) {
        onEditBtnClick(thingAddedAdapter.getData()[position])
        thingAddedAdapter.notifyItemChanged(position)
    }
}
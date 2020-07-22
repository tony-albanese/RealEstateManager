package com.openclassrooms.realestatemanager.Activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.Activities.ListingMapActivities.SingleListingMapActivity
import com.openclassrooms.realestatemanager.ListingPhotos.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.*
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingInformationDetailLayoutBinding
import kotlinx.android.synthetic.main.listing_decription_editor_layout.*
import kotlinx.android.synthetic.main.listing_information_detail_layout.*
import java.io.File

//TODO () Implement recycler view and camera feature in this activity.
class DisplayListingPortaitActivity : AppCompatActivity(), View.OnLongClickListener, ListingPhotoViewModel.OnDatabaseActionResult {

    lateinit var listingViewModel: ListingViewModel
    lateinit var helper: HelperMethods

    lateinit var listingPhotoUtilities: ListingPhotoUtilities
    lateinit var photoRecyclerView: RecyclerView
    lateinit var photoAdapter: ListingPhotoAdapter
    lateinit var listingPhotoViewModel: ListingPhotoViewModel

    lateinit var globalVariables: GlobalVariableApplication

    var photos: ArrayList<ListingPhoto> = ArrayList<ListingPhoto>()
    var imageFile: File? = null
    var unpublishedListings = listOf<Listing>()

    /*
    This is the activity that will be launched if the device is in portrait mode.
    It will display the info for a selected listing.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)

        val factory = PhotoViewModelFactory(application, this, null)
        listingPhotoViewModel = ViewModelProvider(this, factory).get(ListingPhotoViewModel::class.java)
        listingPhotoViewModel.listener = this

        val binding: ListingInformationDetailLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listing_information_detail_layout)
        binding.viewModel = listingViewModel
        binding.lifecycleOwner = this


        portrait_toolbar.setTitle("Your Listing")
        setSupportActionBar(portrait_toolbar)
        portrait_app_bar.visibility = View.VISIBLE

        helper = HelperMethods()
        globalVariables = application as GlobalVariableApplication

        val intent = intent
        intent.getLongExtra("LISTING_ID", 0).let {
            if (it != 0.toLong()) {
                globalVariables.selectedPortraitListingId = it
                listingViewModel.getListingForPortraitMode(it)
            }
        }

        listingPhotoUtilities = ListingPhotoUtilities(this, this)
        photoRecyclerView = findViewById<RecyclerView>(R.id.rv_listing_image_recycler_view)
        photoAdapter = ListingPhotoAdapter(this, photos)


        setListingDescriptionListeners()
        setObservers()

        ib_take_photo?.setOnClickListener {
            takePhoto()
        }
        ib_add_photo_gallery?.setOnClickListener {
            getPhotoFromGallery()
        }
        setupImageRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu?.getItem(0)?.setEnabled(false)
        menu?.getItem(0)?.setVisible(false)
        menu?.getItem(2)?.setEnabled(false)
        menu?.getItem(2)?.setVisible(false)
        menu?.getItem(3)?.setEnabled(false)
        menu?.getItem(3)?.setVisible(false)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        helper.generateUnpublishedListingMenu(menu, 3, unpublishedListings)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit_listing -> {
                helper.onEditListingClick(this, listingViewModel.selectedListing?.value?.id?.toLong()
                        ?: 0)
                finish()
                return true
            }
            else -> return true
        }
    }

    fun setListingDescriptionListeners() {
        val descriptionTextView = findViewById<TextView>(R.id.tv_description_body)
        val confirmImageButton = findViewById<ImageButton>(R.id.ib_confirm_description)
        val cancelImageButton = findViewById<ImageButton>(R.id.ib_cancel_description)
        val editText = findViewById<EditText>(R.id.et_listing_description)

        descriptionTextView.setOnLongClickListener(this)

        confirmImageButton.setOnClickListener {
            listingViewModel.updateListingDescription(editText.text.toString(), updateCallback)
            editText?.text?.clear()
            listing_description_editor_layout?.visibility = View.GONE
        }

        cancelImageButton.setOnClickListener {
            editText?.text?.clear()
            listing_description_editor_layout?.visibility = View.GONE
        }

    }

    val updateCallback: (Int) -> Unit = {

        val snackbar = Snackbar.make(
                findViewById(R.id.display_listing_constraint_layout),
                R.string.update_ok_message, BaseTransientBottomBar.LENGTH_LONG)

        when (it) {
            1 -> snackbar.show()
            else -> {
                snackbar.setText(R.string.update_failed_message)
                        .show()
            }
        }


    }

    override fun onLongClick(view: View?): Boolean {
        val editText = findViewById<EditText>(R.id.et_listing_description)
        editText.setText(listingViewModel.selectedListing.value?.listingDescription ?: "")
        listing_description_editor_layout.visibility = View.VISIBLE
        listing_description_editor_layout.bringToFront()
        return true
    }

    fun setObservers() {
        val listingBodyTextView = findViewById<TextView>(R.id.tv_description_body)
        val imageView = findViewById<ImageView>(R.id.listing_static_map_image_view)
        listingViewModel.selectedListing.observe(this, androidx.lifecycle.Observer {
            listingBodyTextView.text = it.listingDescription
            Glide.with(this)
                    .load(it.listingImageUrl)
                    .error(R.drawable.placeholder_image)
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageView)
        })

        listingViewModel.unpublishedListings.observe(this, androidx.lifecycle.Observer {
            unpublishedListings = it
            invalidateOptionsMenu()
        })

        imageView.setOnClickListener {
            val intent = Intent(this, SingleListingMapActivity::class.java)
            intent.putExtra(LISTING_ID, listingViewModel.selectedListing.value?.id)
            startActivity(intent)
        }
    }

    fun setupImageRecyclerView() {

        photoRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photoRecyclerView?.adapter = photoAdapter

        listingPhotoViewModel.listingPhotos.observe(this, androidx.lifecycle.Observer {
            photos = it as ArrayList<ListingPhoto>
            photoAdapter.photoList = photos
            photoAdapter.notifyDataSetChanged()
        })

    }

    fun takePhoto() {

        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
            )
        } else {
            launchPhotoActivity()
        }
    }

    fun hasCameraPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        )
    }

    fun launchPhotoActivity() {
        val (intent, file) = listingPhotoUtilities.createTakePictureIntent()
        imageFile = file
        intent.resolveActivity(packageManager)?.also {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun getPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY)
    }

    override fun onInsertPhoto(row: Long) {
        listingPhotoViewModel.getPhotosForLisiting(globalVariables.selectedPortraitListingId)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                when {

                    grantResults.isEmpty() -> Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show()

                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        ib_take_photo?.visibility = View.VISIBLE
                        launchPhotoActivity()
                    }
                    else -> {
                        ib_take_photo?.isEnabled = false
                        Toast.makeText(this, "Take from the gallery instead.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            REQUEST_EXTERNAL_WRITE_PERMISSION -> {
                when {
                    grantResults.isEmpty() -> photoUtilities.storageDir = filesDir

                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> photoUtilities.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

                    else -> photoUtilities.storageDir = filesDir
                }
            }
        }
    }//Curly brace for onRequestPermissionResult()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            (REQUEST_IMAGE_CAPTURE == requestCode && resultCode == RESULT_OK) -> {
                if (imageFile?.exists() ?: false) {
                    val uri = Uri.fromFile(imageFile)
                    val photoWindow = ListingPhotoWindow(this, findViewById(R.id.listing_activity_coordinator_layout), uri, listingViewModel.selectedListing.value)
                    photoWindow.listener = this
                    photoWindow.show()
                }
            }

            (REQUEST_IMAGE_FROM_GALLERY == requestCode && resultCode == Activity.RESULT_OK) -> {
                data?.data?.apply {
                    val photoWindow = ListingPhotoWindow(this@DisplayListingPortaitActivity, findViewById(R.id.listing_activity_coordinator_layout), this, listingViewModel.selectedListing.value)
                    photoWindow.listener = this@DisplayListingPortaitActivity
                    photoWindow.show()
                }
            }

        }
    }


}

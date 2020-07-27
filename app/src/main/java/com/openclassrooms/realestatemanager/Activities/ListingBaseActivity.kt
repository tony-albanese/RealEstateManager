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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.Activities.ListingMapActivities.AllListingsMapActivity
import com.openclassrooms.realestatemanager.Activities.ListingMapActivities.SingleListingMapActivity
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.*
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingsActivityLayoutBinding
import kotlinx.android.synthetic.main.listing_decription_editor_layout.*
import kotlinx.android.synthetic.main.listing_information_detail_layout.*
import kotlinx.android.synthetic.main.listing_item_layout.view.*
import kotlinx.android.synthetic.main.listings_activity_layout.*
import kotlinx.android.synthetic.main.listings_information_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

open class ListingBaseActivity : AppCompatActivity(), View.OnLongClickListener, ListingPhotoAdapter.ImageClickCallback, ListingPhotoWindow.PhotoSelectionListener, ListingAdapter.InitialSelection, ListingPhotoViewModel.OnDatabaseActionResult {

    //region Variable declaration
    companion object {
        var database: AppDatabase? = null
    }

    //References to utility objects.
    lateinit var globalVariables: GlobalVariableApplication
    lateinit var helperMethods: HelperMethods

    //Declare objects that are needed to display the listings.
    lateinit var listingViewModel: ListingViewModel
    var recyclerView: RecyclerView? = null
    lateinit var adapter: ListingAdapter
    var unpublishedListings = listOf<Listing>()

    //Objects needed for photo functionality
    lateinit var photoUtilities: ListingPhotoUtilities
    lateinit var listingPhotoViewModel: ListingPhotoViewModel
    var photoRecyclerView: RecyclerView? = null
    lateinit var photoAdapter: ListingPhotoAdapter
    var photos: ArrayList<ListingPhoto> = ArrayList<ListingPhoto>()
    var imageFile: File? = null

    //Misc data about state
    var landscapeMode: Boolean = false
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, "ListingBaseActivity Launched", Toast.LENGTH_LONG).show()
        //Initialize the database.
        database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()


        //Initialize helper and utility objects.
        globalVariables = application as GlobalVariableApplication
        helperMethods = HelperMethods()

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)

        //Inflate the layout first.
        setLayoutBinding()

        //Setup the toolbar.
        setSupportActionBar(toolbar)
        toolbar?.title = title

        //Determine if we're in landscape mode.
        landscapeMode = listing_info_landscape_frame_layout != null

        //Setup the recyclerview and adapter for the listings.
        adapter = ListingAdapter(Locale("EN", "US"), landscapeMode, globalVariables, listingAdapterItemViewClickCallback)
        adapter.initialSelectionCallack = this

        //This recyclerview might be null.
        recyclerView = findViewById(R.id.rv_listings)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView?.addItemDecoration(itemDecor)
        recyclerView?.adapter = adapter
        setListingObservers()

        //Initalize the objects needed for the listing photos.
        val factory = PhotoViewModelFactory(application, listingViewModel.selectedListing.value?.id
                ?: 0, this, null)
        listingPhotoViewModel = ViewModelProvider(this, factory).get(ListingPhotoViewModel::class.java)
        listingPhotoViewModel.listener = this
        photoUtilities = ListingPhotoUtilities(this, this)
        photoRecyclerView = findViewById<RecyclerView>(R.id.rv_listing_image_recycler_view)
        photoAdapter = ListingPhotoAdapter(this, photos)

        setupImageRecyclerView()

        //Set the onClickListener for hte buttons
        ib_take_photo?.setOnClickListener {
            takePhoto()
        }
        ib_add_photo_gallery?.setOnClickListener {
            getPhotoFromGallery()
        }

        //Set up the listeners for adding a listing description.
        setListingDescriptionListeners()
    }

    //region Methods that are for the UI that can be overridden in child classes.
    open fun setLayoutBinding() {
        val binding: ListingsActivityLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listings_activity_layout)
        binding.lifecycleOwner = this
        binding.listingViewModel = listingViewModel
    }

    open fun getAnchorView(): View {
        return findViewById(R.id.listing_activity_coordinator_layout)
    }
    //endregion

    //region Display a listing functionality
    //This is the callback that will run when the user clicks on a listing.
    val listingAdapterItemViewClickCallback: (Listing) -> Unit = {
        listingViewModel.setCurrentListing(it)
        listingPhotoViewModel.setSelectedListing(it)
        globalVariables.selectedListingId = it.id
        globalVariables.selectedPosition = adapter.selectedPosition
    }

    //This method sets all the observers that have to do with displaying the listing.
    private fun setListingObservers() {
        listingViewModel.publishedListings.observe(this, androidx.lifecycle.Observer {
            it?.let {
                adapter.setListings(it)
                adapter.notifyDataSetChanged()
            }
        })

        listingViewModel.unpublishedListings.observe(this, androidx.lifecycle.Observer {
            unpublishedListings = it
            invalidateOptionsMenu()
        })

        val listingBodyTextView: TextView? = findViewById<TextView>(R.id.tv_description_body)
        val staticMapImageView: ImageView? = findViewById<ImageView>(R.id.listing_static_map_image_view)
        listingViewModel.selectedListing.observe(this, androidx.lifecycle.Observer {
            listingBodyTextView?.text = it.listingDescription

            staticMapImageView?.apply {
                Glide.with(this@ListingBaseActivity)
                        .load(it.listingImageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(this)
            }
        })

        staticMapImageView?.setOnClickListener {
            val intent = Intent(this, SingleListingMapActivity::class.java)
            intent.putExtra(LISTING_ID, listingViewModel.selectedListing.value?.id)
            startActivity(intent)
        }

    }

    //This method sets the initial selection (if in landscape mode. If in portrait mode, there is no listing selected.)
    override fun initializeInitialSelection(itemView: View, position: Int, listing: Listing) {
        adapter.initialSelectionInitializedFlag = true
        adapter.selectedView = itemView
        adapter.selectedPosition = position
        listingViewModel.setCurrentListing(listing)
        recyclerView?.scrollToPosition(position)
        itemView.setBackgroundColor(resources.getColor(R.color.colorAccent))
        itemView.tv_listing_item_listing_price
                ?.setTextColor(resources.getColor(R.color.white))
        listingPhotoViewModel.getPhotosForLisiting(listing.id)

    }
    //endregion

    //region Listing Description Functionality
    //This method sets up the listener interfaces for the listing description functionality.
    fun setListingDescriptionListeners() {
        val descriptionTextView: TextView? = findViewById<TextView>(R.id.tv_description_body)
        val confirmImageButton: ImageButton? = findViewById<ImageButton>(R.id.ib_confirm_description)
        val cancelImageButton: ImageButton? = findViewById<ImageButton>(R.id.ib_cancel_description)
        val editText: EditText? = findViewById<EditText>(R.id.et_listing_description)

        descriptionTextView?.setOnLongClickListener(this)

        confirmImageButton?.setOnClickListener {
            listingViewModel.updateListingDescription(editText?.text.toString(), updateCallback)
            listing_description_editor_layout?.visibility = View.GONE
        }

        cancelImageButton?.setOnClickListener {
            listing_description_editor_layout?.visibility = View.GONE
        }

    }

    //This is the callback that for when the user long clicks on the description.
    override fun onLongClick(view: View?): Boolean {
        val editText = findViewById<EditText>(R.id.et_listing_description)
        editText.setText(listingViewModel.selectedListing.value?.listingDescription ?: "")
        editText.bringToFront()
        listing_description_editor_layout.visibility = View.VISIBLE
        listing_description_editor_layout.bringToFront()
        return true
    }

    //This is the callback to let the user know updating the listing description was successful.
    val updateCallback: (Int) -> Unit = {
        val snackbar = Snackbar.make(
                getAnchorView(),
                R.string.update_ok_message, BaseTransientBottomBar.LENGTH_LONG)

        when (it) {
            1 -> snackbar.show()
            else -> {
                snackbar.setText(R.string.update_failed_message)
                        .show()
            }
        }

    }
    //endregion

    //region Initialize menus
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu?.getItem(1)?.setEnabled(landscapeMode)
        menu?.getItem(1)?.setVisible(landscapeMode)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        helperMethods.generateUnpublishedListingMenu(menu, 3, unpublishedListings)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add_listing -> {
                helperMethods.onAddNewListingClick(this)
                finish()
                return true
            }
            R.id.menu_item_edit_listing -> {
                helperMethods.onEditListingClick(this, listingViewModel.selectedListing.value?.id
                        ?: 0.toLong())
                finish()
                return true
            }
            R.id.menu_item_map_view -> {
                val intent = Intent(this, AllListingsMapActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }

            else -> {
                helperMethods.onUnpublishedListingClick(this, unpublishedListings, item.itemId.toLong())
                return true
            }
        }
    }

    //endregion

    //region Listing Photos functionality.

    private fun setupImageRecyclerView() {
        photoRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photoRecyclerView?.adapter = photoAdapter

        photoAdapter.photoTapCallbacks = this

        listingPhotoViewModel.listingPhotos.observe(this, androidx.lifecycle.Observer {
            photos = it as ArrayList<ListingPhoto>
            photoAdapter.photoList = photos
            photoAdapter.notifyDataSetChanged()
        })
    }

    //Callbacks for when the user taps on the images.
    override fun onPhotoLongPress(selectedPhoto: ListingPhoto) {
        //TODO: Only set this if the current user owns the listing.
        selectedPhoto.photoUri?.let {
            val photoWindow = ListingPhotoWindow(this@ListingBaseActivity, getAnchorView(), it, listingViewModel.selectedListing.value, selectedPhoto)
            photoWindow.listener = this@ListingBaseActivity
            photoWindow.show()
        }
    }

    override fun onPhotoTap(selectedPhoto: ListingPhoto) {
        val photoWindow = DisplayPhotoWindow(this, getAnchorView(), selectedPhoto.photoUri)
        photoWindow.show()
    }

    //Callbacks for when the user selects or deletes a photo from the DisplayPhotoWindow object.
    override fun onPhotoSelection(photo: ListingPhoto, isHomeImage: Boolean, isNewPhoto: Boolean) {
        if (isNewPhoto) {
            listingPhotoViewModel.saveListingPhoto(photo)
            Toast.makeText(this, "New photo saved", Toast.LENGTH_LONG).show()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val result = listingPhotoViewModel.updateListingPhoto(photo)
                runOnUiThread {
                    if (result != 0) {
                        Toast.makeText(this@ListingBaseActivity, "Photo Update: ${result}", Toast.LENGTH_LONG).show()
                        listingPhotoViewModel.getPhotosForLisiting(photo.listingId)
                    } else {
                        Toast.makeText(this@ListingBaseActivity, "Photo update failed", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        if (isHomeImage) {
            listingViewModel.selectedListing.value?.apply {
                this.listingMainPhotoUri = photo.photoUri

                CoroutineScope(Dispatchers.IO).launch {
                    async { listingViewModel.updateListing(this@apply) }.await()
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }

    override fun onPhotoDelete(uri: Uri, listingId: Long, resultStatus: Boolean) {
        runOnUiThread {
            if (resultStatus) {
                Toast.makeText(this, "Photo removed.", Toast.LENGTH_LONG).show()
                listingPhotoViewModel.getPhotosForLisiting(listingId)
            } else {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show()
            }
        }

    }

    //endregion

    //region Permissions and Photo Callbacks.
    fun hasCameraPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        )
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
                    val photoWindow = ListingPhotoWindow(this, getAnchorView(), uri, listingViewModel.selectedListing.value)
                    photoWindow.listener = this
                    photoWindow.show()
                }
            }

            (REQUEST_IMAGE_FROM_GALLERY == requestCode && resultCode == Activity.RESULT_OK) -> {
                data?.data?.apply {
                    val photoWindow = ListingPhotoWindow(this@ListingBaseActivity, getAnchorView(), this, listingViewModel.selectedListing.value)
                    photoWindow.listener = this@ListingBaseActivity
                    photoWindow.show()
                }
            }

        }
    }

    override fun onInsertPhoto(row: Long) {
        listingPhotoViewModel.getPhotosForLisiting(globalVariables.selectedListingId)
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

    fun launchPhotoActivity() {
        val (intent, file) = photoUtilities.createTakePictureIntent()
        imageFile = file
        intent.resolveActivity(packageManager)?.also {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun getPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY)
    }
    //endregion
}
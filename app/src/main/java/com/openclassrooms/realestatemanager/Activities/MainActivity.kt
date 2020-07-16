package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.Activities.ListingMapActivities.AllListingsMapActivity
import com.openclassrooms.realestatemanager.Activities.ListingMapActivities.SingleListingMapActivity
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhotoAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhotoUtilities
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhotoWindow
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.*
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingsActivityLayoutBinding
import kotlinx.android.synthetic.main.listing_decription_editor_layout.*
import kotlinx.android.synthetic.main.listing_information_detail_layout.*
import kotlinx.android.synthetic.main.listings_activity_layout.*
import kotlinx.android.synthetic.main.listings_information_layout.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), View.OnLongClickListener {

    //TODO () Implement onPermissionResult()

    //TODO () Implement interface to handle photo from camera.

    //TODO () Add check for camera hardware.


    lateinit var photoUtilities: ListingPhotoUtilities
    lateinit var listingViewModel: ListingViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListingAdapter
    lateinit var helper: HelperMethods

    var imageFile: File? = null
    var unpublishedListings = listOf<Listing>()
    var landscapeMode: Boolean = false

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)
        val binding: ListingsActivityLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listings_activity_layout)
        binding.lifecycleOwner = this
        binding.listingViewModel = listingViewModel

        landscapeMode = listing_info_landscape_frame_layout != null
        recyclerView = findViewById(R.id.rv_listings)
        adapter = ListingAdapter(Locale("EN", "US"), landscapeMode, itemViewOnClickListenerCallback)
        helper = HelperMethods()
        photoUtilities = ListingPhotoUtilities(this, this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)
        recyclerView.adapter = adapter

        setSupportActionBar(toolbar)
        toolbar.title = title

        database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()

        if (landscapeMode) {
            setListingDescriptionListeners()
            setupImageRecyclerView()
            iv_take_photo?.setOnClickListener {
                takePhoto()
            }
        }
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu?.getItem(1)?.setEnabled(landscapeMode)
        menu?.getItem(1)?.setVisible(landscapeMode)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //return super.onPrepareOptionsMenu(menu)
        helper.generateUnpublishedListingMenu(menu, 3, unpublishedListings)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add_listing -> {
                helper.onAddNewListingClick(this)
                finish()
                return true
            }
            R.id.menu_item_edit_listing -> {
                helper.onEditListingClick(this, listingViewModel.selectedListing.value?.id
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
                helper.onUnpublishedListingClick(this, unpublishedListings, item.itemId.toLong())
                return true
            }
        }
    }

    val itemViewOnClickListenerCallback: (Listing) -> Unit = {
        listingViewModel.setCurrentListing(it)
    }

    override fun onLongClick(view: View?): Boolean {
        val editText = findViewById<EditText>(R.id.et_listing_description)
        editText.setText(listingViewModel.selectedListing.value?.listingDescription ?: "")
        editText.bringToFront()
        listing_description_editor_layout.visibility = View.VISIBLE
        listing_description_editor_layout.bringToFront()
        return true
    }

    fun setListingDescriptionListeners() {
        val descriptionTextView = findViewById<TextView>(R.id.tv_description_body)
        val confirmImageButton = findViewById<ImageButton>(R.id.ib_confirm_description)
        val cancelImageButton = findViewById<ImageButton>(R.id.ib_cancel_description)
        val editText = findViewById<EditText>(R.id.et_listing_description)

        descriptionTextView.setOnLongClickListener(this)

        confirmImageButton.setOnClickListener {
            listingViewModel.updateListingDescription(editText.text.toString(), updateCallback)
            listing_description_editor_layout?.visibility = View.GONE
        }

        cancelImageButton.setOnClickListener {
            listing_description_editor_layout?.visibility = View.GONE
        }

    }

    fun setObservers() {

        if (landscapeMode) {
            val listingBodyTextView = findViewById<TextView>(R.id.tv_description_body)
            val imageView = findViewById<ImageView>(R.id.listing_static_map_image_view)
            listingViewModel.selectedListing.observe(this, androidx.lifecycle.Observer {
                listingBodyTextView.text = it.listingDescription

                Glide.with(this)
                        .load(it.listingImageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imageView)
            })

            imageView.setOnClickListener {
                val intent = Intent(this, SingleListingMapActivity::class.java)
                intent.putExtra(LISTING_ID, listingViewModel.selectedListing.value?.id)
                startActivity(intent)
            }
        }

        listingViewModel.publishedListings.observe(this, androidx.lifecycle.Observer {
            it?.let {
                adapter.setListings(it)
            }
        })

        listingViewModel.unpublishedListings.observe(this, androidx.lifecycle.Observer {
            unpublishedListings = it
            invalidateOptionsMenu()
        })
    }

    val updateCallback: (Int) -> Unit = {
        val snackbar = Snackbar.make(
                findViewById(R.id.listing_activity_coordinator_layout),
                R.string.update_ok_message, LENGTH_LONG)

        when (it) {
            1 -> snackbar.show()
            else -> {
                snackbar.setText(R.string.update_failed_message)
                        .show()
            }
        }

    }

    fun setupImageRecyclerView() {
        val photoRecyclerView = findViewById<RecyclerView>(R.id.rv_listing_image_recycler_view)
        val photoAdapter = ListingPhotoAdapter(this)
        photoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photoRecyclerView.adapter = photoAdapter
    }

    fun hasCameraPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        )
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                when {

                    grantResults.isEmpty() -> Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show()

                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        //TODO () Make the camera icon visible.
                        //TODO () Set the click listener on the button to take photo.
                        launchPhotoActivity()
                    }
                    else -> {//TODO() Set the click listener to set the image from the gallery.
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

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (imageFile?.exists() ?: false) {
                val photoWindow = ListingPhotoWindow(this, findViewById(R.id.listing_activity_coordinator_layout))
                photoWindow.show()
                //val uri = Uri.fromFile(imageFile)
                //photoUtilities.addPhotoToGallery(this, uri)
            }
        }

    }

    fun launchPhotoActivity() {
        val (intent, file) = photoUtilities.createTakePictureIntent()
        imageFile = file
        intent.resolveActivity(packageManager)?.also {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }
}
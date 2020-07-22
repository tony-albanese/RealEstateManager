package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhoto
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhotoAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhotoUtilities
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.HelperMethods
import com.openclassrooms.realestatemanager.Utilities.LISTING_ID
import com.openclassrooms.realestatemanager.Utilities.REQUEST_CAMERA_PERMISSION
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingInformationDetailLayoutBinding
import kotlinx.android.synthetic.main.listing_decription_editor_layout.*
import kotlinx.android.synthetic.main.listing_information_detail_layout.*
import java.io.File

//TODO () Implement recycler view and camera feature in this activity.
class DisplayListingPortaitActivity : AppCompatActivity(), View.OnLongClickListener {

    lateinit var listingViewModel: ListingViewModel
    lateinit var helper: HelperMethods

    lateinit var listingPhotoUtilities: ListingPhotoUtilities
    lateinit var photoRecyclerView: RecyclerView
    lateinit var photoAdapter: ListingPhotoAdapter

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

        val binding: ListingInformationDetailLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listing_information_detail_layout)
        binding.viewModel = listingViewModel
        binding.lifecycleOwner = this


        portrait_toolbar.setTitle("Your Listing")
        setSupportActionBar(portrait_toolbar)
        portrait_app_bar.visibility = View.VISIBLE

        helper = HelperMethods()

        val intent = intent
        intent.getLongExtra("LISTING_ID", 0).let {
            if (it != 0.toLong()) {
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

    
}

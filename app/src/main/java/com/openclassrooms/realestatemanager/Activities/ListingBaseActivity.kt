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
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.GlobalVariableApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.HelperMethods
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingsActivityLayoutBinding
import kotlinx.android.synthetic.main.listing_decription_editor_layout.*
import kotlinx.android.synthetic.main.listings_activity_layout.*
import kotlinx.android.synthetic.main.listings_information_layout.*
import java.util.*

class ListingBaseActivity : AppCompatActivity(), View.OnLongClickListener {

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


    //Misc data about state
    var landscapeMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val binding: ListingsActivityLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listings_activity_layout)
        binding.lifecycleOwner = this
        binding.listingViewModel = listingViewModel

        //Setup the toolbar.
        setSupportActionBar(toolbar)
        toolbar.title = title

        //Determine if we're in landscape mode.
        landscapeMode = listing_info_landscape_frame_layout != null

        //Setup the recyclerview and adapter for the listings.
        adapter = ListingAdapter(Locale("EN", "US"), landscapeMode, globalVariables, listingAdapterItemViewClickCallback)

        //This recyclerview might be null.
        recyclerView = findViewById(R.id.rv_listings)
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this.context)
            val itemDecor = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecor)
            adapter = adapter
        }

        setListingObservers()

    }

    //region Display a listing functionality
    //This is the callback that will run when the user clicks on a listing.
    val listingAdapterItemViewClickCallback: (Listing) -> Unit = {
        listingViewModel.setCurrentListing(it)
        //listingPhotoViewModel.setSelectedListing(it)
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

    }

    //endregion

    //region Listing Description Functionality
    //This method sets up the listener interfaces for the listing description functionality.
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
                findViewById(R.id.listing_activity_coordinator_layout),
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
            R.id.menu_item_search_listings -> {
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
    private fun setListingPhotoObservers() {

    }

    private fun setupImageRecyclerView() {

    }

    //endregion
}
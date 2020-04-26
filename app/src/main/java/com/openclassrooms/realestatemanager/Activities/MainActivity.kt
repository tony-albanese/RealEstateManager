package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
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

class MainActivity : AppCompatActivity(), View.OnLongClickListener {

    lateinit var listingViewModel: ListingViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListingAdapter
    lateinit var helper: HelperMethods

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

        if (landscapeMode) setListingDescriptionListeners()
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
        listing_description_editor_layout.visibility = View.VISIBLE
        listing_description_editor_layout.bringToFront()
        return true
    }

    //TODO: See if you can move this out of the activity.
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

    fun setObservers() {

        if (landscapeMode) {
            val listingBodyTextView = findViewById<TextView>(R.id.tv_description_body)
            listingViewModel.selectedListing.observe(this, androidx.lifecycle.Observer {
                listingBodyTextView.text = it.listingDescription
            })
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
    
}
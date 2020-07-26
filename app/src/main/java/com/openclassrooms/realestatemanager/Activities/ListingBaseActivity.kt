package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.GlobalVariableApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.HelperMethods
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingsActivityLayoutBinding
import kotlinx.android.synthetic.main.listings_activity_layout.*
import kotlinx.android.synthetic.main.listings_information_layout.*
import java.util.*

class ListingBaseActivity : AppCompatActivity() {

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


    }

    //This is the callback that will run when the user clicks on a listing.
    val listingAdapterItemViewClickCallback: (Listing) -> Unit = {
        listingViewModel.setCurrentListing(it)
        //listingPhotoViewModel.setSelectedListing(it)
        globalVariables.selectedListingId = it.id
        globalVariables.selectedPosition = adapter.selectedPosition
    }
}
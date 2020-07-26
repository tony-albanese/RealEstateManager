package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.openclassrooms.realestatemanager.DisplayListings.ListingAdapter
import com.openclassrooms.realestatemanager.ListingPhotos.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.HelperMethods
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import java.io.File

class BaseListingActivity : AppCompatActivity() {


    //Declare references to objects needed to display listing photos.
    lateinit var photoUtilities: ListingPhotoUtilities
    lateinit var listingPhotoViewModel: ListingPhotoViewModel
    var photoRecyclerView: RecyclerView? = null
    lateinit var photoAdapter: ListingPhotoAdapter
    var photos: ArrayList<ListingPhoto> = ArrayList<ListingPhoto>()
    var imageFile: File? = null

    //Declare references to objects related to the selected listing.
    lateinit var listingViewModel: ListingViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListingAdapter
    var unpublishedListings = listOf<Listing>()

    //Declare references to utility objects.
    lateinit var helper: HelperMethods
    lateinit var globalVariables: GlobalVariableApplication


    var landscapeMode: Boolean = false


    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.listings_activity_layout)

        //Initialize the Room database.
        MainActivity.database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()

        
    }
}
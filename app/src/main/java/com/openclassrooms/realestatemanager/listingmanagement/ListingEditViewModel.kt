package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.ListingRepository

/*
This class is designed to preserve the UI state when editing
a listing.
 */

class ListingEditViewModel(
        private val application: Application,
        private val handle: SavedStateHandle
) : ViewModel() {


    var address: String
    val repository: ListingRepository

    init {


        address = handle["tv_listing_street_address"] ?: ""


        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)
    }

    fun saveSpinnerPostion(position: Int) {
        //handle.set()
    }

}
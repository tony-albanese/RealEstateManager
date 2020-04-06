package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingRepository

/*
This class is designed to preserve the UI state when editing
a listing.
 */

class ListingEditViewModel(
        private val application: Application,
        private val handle: SavedStateHandle,
        val listingId: Long
) : ViewModel() {


    val testBindingVariable: String = "This is a test value."
    val repository: ListingRepository

    val currentListing: MutableLiveData<Listing>  //This is the member variable that will be exposed to the outside world.

    init {
        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)

        //TODO: Get the current listing from the repository and cast it in a MutableLiveData object.
        currentListing = MutableLiveData(Listing())
    }

}




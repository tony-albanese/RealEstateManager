package com.openclassrooms.realestatemanager.database_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ListingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ListingRepository
    val listings: LiveData<List<Listing>>

    init {
        val listingDao = RealEstateApplication.database?.listingDao()
        repository = ListingRepository(listingDao!!)
        listings = repository.allListings
    }


    fun insert(listing: Listing) = viewModelScope.launch {
        repository.insert(listing)
    }
}
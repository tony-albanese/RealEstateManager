package com.openclassrooms.realestatemanager.database_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ListingRepository
    val listings: LiveData<List<Listing>>
    val publishedListings: LiveData<List<Listing>>

    private val _selectedListing = MutableLiveData<Listing>()
    val selectedListing: LiveData<Listing>
        get() = _selectedListing


    init {
        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)
        listings = repository.allListings
        publishedListings = repository.publishedListings
        _selectedListing.value = Listing()
    }

    fun insert(listing: Listing) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(listing)
    }

    fun clearDatabase() = viewModelScope.launch(Dispatchers.IO) {
        repository.delete()
    }

    fun getListingById(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.getListingById(id).value
    }

    fun setCurrentListing(listing: Listing) {
        _selectedListing.value = listing
    }
}
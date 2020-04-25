package com.openclassrooms.realestatemanager.database_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListingViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

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

    fun getListingForPortraitMode(id: Long) = viewModelScope.launch {
        val listing = repository.getListing(id)
        setCurrentListing(listing)
    }

    fun updateListingDescription(description: String) {
        _selectedListing.value?.listingDescription = description
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateListing(_selectedListing.value!!)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}
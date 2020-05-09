package com.openclassrooms.realestatemanager.database_files

import android.app.Application
import android.util.Log
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
    val unpublishedListings: LiveData<List<Listing>>

    private val _selectedListing = MutableLiveData<Listing>()
    val selectedListing: LiveData<Listing>
        get() = _selectedListing


    init {
        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)
        listings = repository.allListings
        publishedListings = repository.publishedListings
        unpublishedListings = repository.unpublishedListings
        _selectedListing.value = Listing()
    }

    fun insert(listing: Listing) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(listing)
    }

    fun clearDatabase() = viewModelScope.launch(Dispatchers.IO) {
        repository.delete()
    }

    suspend fun getListingById(id: Long): Listing {
        return repository.getListing(id)
    }

    fun setCurrentListing(listing: Listing) {
        //_selectedListing.value = listing
        _selectedListing.postValue(listing)
    }

    fun getListingForPortraitMode(id: Long) = viewModelScope.launch {
        Log.i("GEOCODE", "getListingforPortraitMode called")
        val listing = repository.getListing(id)
        setCurrentListing(listing)
    }

    fun updateListingDescription(description: String, onUpdate: (Int) -> Unit) {
        _selectedListing?.value?.listingDescription = description
        _selectedListing.postValue(_selectedListing.value)

        viewModelScope.launch {
            val result = repository.updateListing(_selectedListing.value!!)
            onUpdate(result)
        }

    }

    suspend fun updateListing(listing: Listing): Int {
        return repository.updateListing(listing)
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}
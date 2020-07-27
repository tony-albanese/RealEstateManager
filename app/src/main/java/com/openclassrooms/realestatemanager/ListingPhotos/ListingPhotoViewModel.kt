package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ListingPhotoViewModel(val application: Application, id: Long = 0) : ViewModel(

), CoroutineScope {
    val repository: ListingPhotoRepository
    var listener: OnDatabaseActionResult? = null
    val globalVariables = application as GlobalVariableApplication

    private val _selectedListing = MutableLiveData<Listing>()
    val selectedListing: LiveData<Listing>
        get() = _selectedListing

    private val _listingPhotos = MutableLiveData<List<ListingPhoto>>()
    val listingPhotos: LiveData<List<ListingPhoto>>
        get() = _listingPhotos

    init {
        val listingPhotoDao = AppDatabase.getDatabase(application).listingPhotoDao()
        repository = ListingPhotoRepository(listingPhotoDao)
        if (!id.equals(0)) {
            getPhotosForLisiting(id)
        } else {
            getPhotosForLisiting(globalVariables.selectedListingId)
        }


    }
    
    fun saveListingPhoto(photo: ListingPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = async { repository.insertPhoto(photo) }.await()
            listener?.onInsertPhoto(id)
        }

    }

    fun getPhotosForLisiting(listingId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val photos = async { repository.getPhotosForListing(listingId) }.await()
            withContext(Dispatchers.Main) {
                _listingPhotos.value = photos
            }
        }

    }

    fun setSelectedListing(listing: Listing) {
        _selectedListing.postValue(listing)
        getPhotosForLisiting(listing.id)
    }

    suspend fun updateListingPhoto(photo: ListingPhoto): Int {
        return repository.upddateListingPhoto(photo)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    
    interface OnDatabaseActionResult {
        fun onInsertPhoto(row: Long)
    }
}
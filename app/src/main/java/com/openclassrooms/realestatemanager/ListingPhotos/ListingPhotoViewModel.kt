package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListingPhotoViewModel(val application: Application) : ViewModel(

), CoroutineScope {
    val repository: ListingPhotoRepository
    var listener: OnDatabaseActionResult? = null

    private val _listingPhotos = MutableLiveData<List<ListingPhoto>>()
    val listingPhotos: LiveData<List<ListingPhoto>>
        get() = _listingPhotos

    init {
        val listingPhotoDao = AppDatabase.getDatabase(application).listingPhotoDao()
        repository = ListingPhotoRepository(listingPhotoDao)
    }


    fun saveListingPhoto(photo: ListingPhoto) {
        viewModelScope.launch {
            val id = async { repository.insertPhoto(photo) }.await()
            listener?.onInsertPhoto(id)
        }

    }

    fun getPhotosForLisiting(listingId: Long) {
        //_listingPhotos.postValue()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    interface OnDatabaseActionResult {
        fun onInsertPhoto(row: Long)
    }
}
package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class ListingPhotoViewModel(val application: Application) : ViewModel(

), CoroutineScope {
    val repository: ListingPhotoRepository

    private val _listingPhotos = MutableLiveData<List<ListingPhoto>>()
    val listingPhotos: LiveData<List<ListingPhoto>>
        get() = _listingPhotos

    init {
        val listingPhotoDao = AppDatabase.getDatabase(application).listingPhotoDao()
        repository = ListingPhotoRepository(listingPhotoDao)
    }


    fun saveListingPhoto(photo: ListingPhoto) {
        //repository.insertPhoto(photo)
    }

    fun getPhotosForLisiting(listingId: Long) {
        //_listingPhotos.postValue()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}
package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database_files.AppDatabase

class ListingPhotoViewModel(val application: Application) : ViewModel(

) {
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


    //TODO () Need a method to save a ListingPhoto object.
    //TODO () Need a method to fetch all of the listing photo objects for a particular listing (by Id)
    //TODO () Expose all of the listing photos for a listing via LiveData.
}
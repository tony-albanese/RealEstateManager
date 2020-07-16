package com.openclassrooms.realestatemanager.ListingPhotos

import androidx.lifecycle.LiveData

class ListingPhotoRepository(
        private val listingPhotoDao: ListingPhotoDao
) {

    val allPhotos = listingPhotoDao.getAllListingPhotos()

    suspend fun insertPhoto(photo: ListingPhoto): Long {
        return listingPhotoDao.insertListingPhoto(photo)
    }

    suspend fun getPhotosForListing(listingId: Long): LiveData<List<ListingPhoto>> {
        return listingPhotoDao.getListingPhotosByListingId(listingId)
    }

}
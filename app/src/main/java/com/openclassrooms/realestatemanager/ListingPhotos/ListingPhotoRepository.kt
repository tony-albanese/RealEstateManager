package com.openclassrooms.realestatemanager.ListingPhotos

import android.net.Uri

class ListingPhotoRepository(
        private val listingPhotoDao: ListingPhotoDao
) {

    val allPhotos = listingPhotoDao.getAllListingPhotos()

    suspend fun insertPhoto(photo: ListingPhoto): Long {
        return listingPhotoDao.insertListingPhoto(photo)
    }

    suspend fun getPhotosForListing(listingId: Long): List<ListingPhoto> {
        return listingPhotoDao.getListingPhotosByListingId(listingId)
    }

    suspend fun getPhotoByUri(uri: Uri): ListingPhoto {
        return listingPhotoDao.getPhotoByUri(uri)
    }

    suspend fun deleteListingPhoto(uri: Uri): Int {
        return listingPhotoDao.deleteListingPhoto(uri)
    }

    suspend fun upddateListingPhoto(photo: ListingPhoto): Int {
        return listingPhotoDao.updatePhoto(photo)
    }
}
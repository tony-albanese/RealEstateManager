package com.openclassrooms.realestatemanager.ListingPhotos

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

}
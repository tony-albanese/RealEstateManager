package com.openclassrooms.realestatemanager.database_files

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListingRepository(
        private val listingDao: ListingDao
) {
    val allListings = listingDao.getAllListings()
    val publishedListings = listingDao.getPublishedListings()

    suspend fun insert(listing: Listing): Long = withContext(Dispatchers.IO) {
        listingDao.insertListing(listing)
    }

    suspend fun delete() {
        listingDao.deleteAllListings()
    }

    suspend fun getListingById(id: Long): LiveData<Listing> =
            withContext(Dispatchers.IO) {
                listingDao.getListingById(id)
            }

    suspend fun getListing(id: Long): Listing = withContext(Dispatchers.IO) {
        listingDao.getListing(id)
    }

    suspend fun updateListing(listing: Listing): Int = withContext(Dispatchers.IO) {
        listingDao.updateListing(listing)
    }

}
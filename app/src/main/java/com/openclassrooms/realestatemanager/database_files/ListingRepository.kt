package com.openclassrooms.realestatemanager.database_files

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListingRepository(
        private val listingDao: ListingDao
) {
    val allListings = listingDao.getAllListings()

    suspend fun insert(listing: Listing): Long = withContext(Dispatchers.IO) {
        listingDao.insertListing(listing)
    }

    suspend fun delete() {
        listingDao.deleteAllListings()
    }

    suspend fun getListingById(id: Long): Listing {
        return listingDao.getListingById(id)
    }

}
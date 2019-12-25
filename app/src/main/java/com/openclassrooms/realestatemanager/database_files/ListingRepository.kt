package com.openclassrooms.realestatemanager.database_files

class ListingRepository(
        private val listingDao: ListingDao
) {
    val allListings = listingDao.getAllListings()

    suspend fun insert(listing: Listing) {
        listingDao.insertListing(listing)
    }

}
package com.openclassrooms.realestatemanager.database_files

class ListingRepository(
        private val listingDao: ListingDao
) {
    val allListings = listingDao.getAllListings()

    suspend fun insert(listing: Listing): Long {
        return listingDao.insertListing(listing)
    }

    suspend fun delete() {
        listingDao.deleteAllListings()
    }

    suspend fun getListingById(id: Long): Listing {
        return listingDao.getListingById(id)
    }

}
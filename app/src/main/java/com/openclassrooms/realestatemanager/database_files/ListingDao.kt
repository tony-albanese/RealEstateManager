package com.openclassrooms.realestatemanager.database_files

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListingDao {

    @Query("SELECT * FROM table_listings")
    fun getAllListings(): List<Listing>

    @Insert
    fun insertListing(listing: Listing)

    @Query("DELETE FROM table_listings")
    fun deleteAllListings()

    @Query("SELECT * FROM table_listings WHERE id = :id")
    fun getListingById(id: Long): Listing

}
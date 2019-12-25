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
}
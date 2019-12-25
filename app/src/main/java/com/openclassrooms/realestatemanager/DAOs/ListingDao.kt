package com.openclassrooms.realestatemanager.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.databases.Listing

@Dao
interface ListingDao {

    @Query("SELECT * FROM table_listings")
    fun getAllListings(): List<Listing>

    @Insert
    fun insertListing(listing: Listing)
}
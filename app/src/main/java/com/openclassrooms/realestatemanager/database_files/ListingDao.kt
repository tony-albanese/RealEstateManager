package com.openclassrooms.realestatemanager.database_files

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListingDao {

    @Query("SELECT * FROM table_listings")
    fun getAllListings(): LiveData<List<Listing>>

    @Insert
    fun insertListing(listing: Listing)

    @Query("DELETE FROM table_listings")
    fun deleteAllListings()
}
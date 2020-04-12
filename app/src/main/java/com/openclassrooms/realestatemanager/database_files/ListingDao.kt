package com.openclassrooms.realestatemanager.database_files

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListingDao {

    //TODO: Add signature for a method to select only the published listings.
    
    @Query("SELECT * FROM table_listings")
    fun getAllListings(): LiveData<List<Listing>>

    /*
    Added this function for testing purposes. It does not return live data.
     */
    @Query("SELECT * FROM table_listings")
    fun getListings(): List<Listing>

    @Insert
    fun insertListing(listing: Listing): Long

    @Query("DELETE FROM table_listings")
    fun deleteAllListings()

    @Query("SELECT * FROM table_listings WHERE id = :id")
    fun getListingById(id: Long): Listing

}
package com.openclassrooms.realestatemanager.database_files

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListingDao {
    
    @Query("SELECT * FROM table_listings WHERE listing_is_published = :isPublished")
    fun getPublishedListings(isPublished: Boolean = true): LiveData<List<Listing>>
    
    @Query("SELECT * FROM table_listings")
    fun getAllListings(): LiveData<List<Listing>>

    /*
    Added this function for testing purposes. It does not return live data.
     */
    @Query("SELECT * FROM table_listings")
    fun getListings(): List<Listing>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertListing(listing: Listing): Long

    @Update
    fun updateListing(listing: Listing): Int

    @Query("DELETE FROM table_listings")
    fun deleteAllListings()

    @Query("SELECT * FROM table_listings WHERE id = :id")
    fun getListingById(id: Long): LiveData<Listing>

    @Query("SELECT * FROM table_listings WHERE id = :id")
    fun getListing(id: Long): Listing

}
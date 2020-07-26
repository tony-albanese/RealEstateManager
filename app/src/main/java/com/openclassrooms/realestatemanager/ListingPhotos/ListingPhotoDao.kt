package com.openclassrooms.realestatemanager.ListingPhotos

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListingPhotoDao {

    @Query("SELECT * FROM table_listing_photos WHERE listing_id = :listingId")
    fun getListingPhotosByListingId(listingId: Long = 0): List<ListingPhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListingPhoto(photo: ListingPhoto): Long

    @Update
    fun updatePhoto(photo: ListingPhoto): Int

    @Query("SELECT * from table_listing_photos")
    fun getAllListingPhotos(): LiveData<List<ListingPhoto>>

    @Query("SELECT * FROM table_listing_photos WHERE photo_uri = :uri")
    fun getPhotoByUri(uri: Uri): ListingPhoto

    @Query("DELETE FROM table_listing_photos WHERE photo_uri = :uri")
    fun deleteListingPhoto(uri: Uri): Int
}
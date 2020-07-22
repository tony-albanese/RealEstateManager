package com.openclassrooms.realestatemanager.ListingPhotos

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_listing_photos")
data class ListingPhoto(
        @ColumnInfo(name = "photo_id") @PrimaryKey(autoGenerate = true) var photoId: Long = 0,
        @ColumnInfo(name = "listing_id") var listingId: Long = 0,
        @ColumnInfo(name = "photo_description") var photoDescription: String = "",
        @ColumnInfo(name = "photo_uri") var photoUri: Uri? = null
) {

}
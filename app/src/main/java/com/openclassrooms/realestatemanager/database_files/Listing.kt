package com.openclassrooms.realestatemanager.database_files

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_listings")
data class Listing(
        @ColumnInfo(name = "listing_description")
        var listingDescription: String = "",

        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0
) {
}
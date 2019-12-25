package com.openclassrooms.realestatemanager.database_files

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Listing::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listingDao(): ListingDao
}
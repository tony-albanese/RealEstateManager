package com.openclassrooms.realestatemanager.database_files

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.Utilities.ListingDataTypeConverters

@Database(entities = [Listing::class], version = 1)
@TypeConverters(ListingDataTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listingDao(): ListingDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "listing-database"
                )
                        .build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
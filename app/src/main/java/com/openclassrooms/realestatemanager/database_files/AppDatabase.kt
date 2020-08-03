package com.openclassrooms.realestatemanager.database_files

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhoto
import com.openclassrooms.realestatemanager.ListingPhotos.ListingPhotoDao
import com.openclassrooms.realestatemanager.Login.Agent
import com.openclassrooms.realestatemanager.Login.LoginDao
import com.openclassrooms.realestatemanager.Utilities.DatabaseTypeConverter

@Database(entities = [Listing::class, ListingPhoto::class, Agent::class], version = 1)
@TypeConverters(DatabaseTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listingDao(): ListingDao
    abstract fun listingPhotoDao(): ListingPhotoDao
    abstract fun loginDao(): LoginDao

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
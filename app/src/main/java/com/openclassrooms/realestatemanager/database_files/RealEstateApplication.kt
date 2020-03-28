package com.openclassrooms.realestatemanager.database_files

import android.app.Application
import androidx.room.Room


class RealEstateApplication : Application() {
    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()
    }
}
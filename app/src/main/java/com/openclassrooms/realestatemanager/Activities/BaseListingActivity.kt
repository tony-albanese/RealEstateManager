package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.AppDatabase

class BaseListingActivity : AppCompatActivity() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.listings_activity_layout)


        //Initialize the Room database.
        MainActivity.database = Room.databaseBuilder(this,
                AppDatabase::class.java,
                "listing-db")
                .build()

    }
}
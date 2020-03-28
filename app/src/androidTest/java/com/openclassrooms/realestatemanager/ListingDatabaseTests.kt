package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.ListingDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ListingDatabaseTests {

    private lateinit var listingDao: ListingDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
        listingDao = db.listingDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}

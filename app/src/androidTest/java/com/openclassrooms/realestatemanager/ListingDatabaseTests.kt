package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingDao
import org.junit.After
import org.junit.Before
import org.junit.Test
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

    @Test
    @Throws(Exception::class)
    fun writeAndReadListingById() {
        val listing = Listing()
        listing.listingDescription = "description"
        listingDao.insertListing(listing)

        val listings = listingDao.getListings()
        assert(listings.isNotEmpty())
    }

    //TODO Need to test live data.

    @Test
    @Throws(Exception::class)
    fun testCreatingIdUponInsert() {
        val listing1 = Listing()
        listing1.listingDescription = "description"
        val id = listingDao.insertListing(listing1)
        assert(!id.equals(0))
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndRetrieveListing() {
        val listing = Listing()
        listing.listingDescription = "description"
        val id = listingDao.insertListing(listing)
        val retrievedListing = listingDao.getListingById(id)
        assert(retrievedListing.value?.listingDescription.equals(listing.listingDescription))
    }


    @Test
    @Throws(Exception::class)
    fun testDeleteDatabase() {
        val listing1 = Listing()
        val listing2 = Listing()
        listingDao.insertListing(listing1)
        listingDao.insertListing(listing2)

        listingDao.deleteAllListings()
        val listings = listingDao.getListings()
        assert(listings.isEmpty())
    }
}

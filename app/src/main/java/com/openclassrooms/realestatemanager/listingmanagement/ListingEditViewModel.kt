package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.ListingRepository

/*
This class is designed to preserve the UI state when editing
a listing.
 */

class ListingEditViewModel(
        private val application: Application,
        private val handle: SavedStateHandle
) : ViewModel() {

    val SPINNER_POSITION_KEY: String = "spinner_position_key"
    val TOTAL_ROOMS_KEY: String = "total_rooms_key"
    val BEDROOMS_KEY: String = "bedrooms_key"

    var streetAddress: String
    var zipCode: String
    var city: String
    var salesPrice: String
    val repository: ListingRepository

    init {


        streetAddress = handle["tv_listing_street_address"] ?: ""
        zipCode = handle["et_listing_city"] ?: ""
        city = handle["et_listing_zipcode"] ?: ""
        salesPrice = handle["et_listing_sales_price"] ?: ""


        val listingDao = AppDatabase.getDatabase(application).listingDao()
        repository = ListingRepository(listingDao)
    }


    fun saveSpinnerPosition(position: Int) {
        handle.set(SPINNER_POSITION_KEY, position)
    }

    fun getSpinnerPosition(): Int {
        return handle.get(SPINNER_POSITION_KEY) ?: 0
    }

    fun saveNumberOfRooms(rooms: Int) {
        handle.set(TOTAL_ROOMS_KEY, rooms)
    }


    fun getNumberOfRooms(): Int {
        return handle.get(TOTAL_ROOMS_KEY) ?: 0
    }

    fun saveNumberOfBedroom(bedrooms: Int) {
        handle.set(BEDROOMS_KEY, bedrooms)
    }

    fun getBedrooms(): Int {
        return handle.get(BEDROOMS_KEY) ?: 0
    }

}
package com.openclassrooms.realestatemanager.listingmanagement

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Utilities.SPINNER_POSITION_KEY
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


    fun setSpinnerPostion(position: Int) {
        handle.set(SPINNER_POSITION_KEY, position)
    }

    fun getSpinnerPosition(): Int {
        return handle.get(SPINNER_POSITION_KEY) ?: 0
    }

}
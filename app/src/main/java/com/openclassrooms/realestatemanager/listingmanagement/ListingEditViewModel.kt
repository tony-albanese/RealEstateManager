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
    val BATHROOMS_KEY: String = "bathrooms_key"
    val FOR_SALE_KEY: String = "for_sale_key"
    val IS_PUBLISHED_KEY: String = "is_published_key"

    var streetAddress: String
    var zipCode: String
    var city: String
    var salesPrice: String
    var isForSale: Boolean
    var isPublished: Boolean

    val repository: ListingRepository


    init {


        streetAddress = handle["tv_listing_street_address"] ?: ""
        zipCode = handle["et_listing_city"] ?: ""
        city = handle["et_listing_zipcode"] ?: ""
        salesPrice = handle["et_listing_sales_price"] ?: ""

        isForSale = handle.get(FOR_SALE_KEY) ?: true
        isPublished = handle.get(IS_PUBLISHED_KEY) ?: true


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

    fun saveNumberOfBathrooms(bathrooms: Double) {
        handle.set(BATHROOMS_KEY, bathrooms)
    }

    fun getBathrooms(): Double {
        return handle.get(BATHROOMS_KEY) ?: 1.5
    }

    fun saveForSaleState(isForSale: Boolean) {
        handle.set(FOR_SALE_KEY, isForSale)
    }

    fun getForSaleState(): Boolean {
        return handle.get(FOR_SALE_KEY) ?: true
    }

    fun saveIsPublishedState(isPublished: Boolean) {
        handle.set(IS_PUBLISHED_KEY, isPublished)
    }

    fun getIsPublishedState(): Boolean {
        return handle.get(IS_PUBLISHED_KEY) ?: true
    }
}
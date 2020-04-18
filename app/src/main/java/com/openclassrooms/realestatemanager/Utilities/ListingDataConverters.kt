package com.openclassrooms.realestatemanager.Utilities

import androidx.databinding.InverseMethod
import com.openclassrooms.realestatemanager.database_files.Listing

object ListingDataTypeConverters {

    @InverseMethod("bathroomsToProgress")
    @JvmStatic
    fun progressToNumberOfBathrooms(progress: Int): Double {
        return (progress / 2).toDouble()
    }

    @JvmStatic
    fun bathroomsToProgress(numberOfBathrooms: Double): Int {
        return (numberOfBathrooms * 2).toInt()
    }

    @InverseMethod("areaIntToString")
    @JvmStatic
    fun areaStringToInt(areaString: String): Int {
        if (areaString.isNullOrBlank() || areaString.isEmpty()) {
            return 0
        } else {
            val filteredString = areaString.filter {
                it.isDigit()
            }
            return filteredString.toInt()
        }
    }

    @JvmStatic
    fun areaIntToString(value: Int): String {
        return value.toString()
    }

    @JvmStatic
    fun generateAreaStringFromInt(value: Int): String {
        return "Total Area: " + value.toString()
    }

    @JvmStatic
    fun generateRoomsStringFromInt(value: Int): String {
        return "Number of Rooms: " + value.toString()
    }

    @JvmStatic
    fun generateNumberOfBathroomsString(value: Double): String {
        return "Number of Bathrooms: " + value.toString()
    }

    @JvmStatic
    fun generateNumberOfBedroomsString(value: Int): String {
        return "Number of Bedrooms: " + value.toString()
    }

    @JvmStatic
    fun generateAddressString(listing: Listing): String {
        val builder = StringBuilder()
        builder.appendln(listing.listingStreetAddress)
                .appendln(listing.listingCity)
                .appendln(listing.listingZipCode)

        return builder.toString()
    }
}
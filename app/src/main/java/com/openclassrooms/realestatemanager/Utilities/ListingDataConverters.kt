package com.openclassrooms.realestatemanager.Utilities

import androidx.databinding.InverseMethod

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
}
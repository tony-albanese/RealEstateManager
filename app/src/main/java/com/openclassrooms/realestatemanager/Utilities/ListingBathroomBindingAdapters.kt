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
}
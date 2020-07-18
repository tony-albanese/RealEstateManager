package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Application

class GlobalVariableApplication() : Application() {

    var selectedListingId: Long = 0
    var selectedPosition: Int = 0

    override fun onCreate() {
        super.onCreate()
    }

}
package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R

class DisplayListingPortaitActivity : AppCompatActivity() {

    /*
    This is the activity that will be launched if the device is in portrait mode.
    It will display the info for a selected listing.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: Inflate the layout to display the listing information.
        setContentView(R.layout.activity_main)
    }
}

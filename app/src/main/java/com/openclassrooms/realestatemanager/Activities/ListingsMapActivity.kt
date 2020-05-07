package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.ACTIVITY_TASK
import com.openclassrooms.realestatemanager.Utilities.LISTING_ID
import com.openclassrooms.realestatemanager.Utilities.TASK_SELECT_LISTING_LOCATION
import kotlinx.android.synthetic.main.activity_listings_map.*

class ListingsMapActivity : AppCompatActivity() {

    var incomingIntent: Intent? = null
    var activityTask: Int = 0
    var listingId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapquest_token))
        setContentView(R.layout.activity_listings_map)

        incomingIntent = intent
        incomingIntent?.let {
            val activityTaskCode = it.getIntExtra(ACTIVITY_TASK, 0)
            activityTask = activityTaskCode
            listingId = it.getLongExtra(LISTING_ID, 0)
        }

        //TODO: Setup ViewModel
        map_view?.getMapAsync(mapReadyCallback)
    }

    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(mapboxMap: MapboxMap) {
            mapboxMap.setStyle(Style.MAPBOX_STREETS)

            when (activityTask) {
                TASK_SELECT_LISTING_LOCATION -> {
                    displayPossibleListingLocationMarkers(mapboxMap)
                }
            }
        }
    }


    fun displayPossibleListingLocationMarkers(map: MapboxMap) {
        Toast.makeText(this, "displaying possible lisitngs", Toast.LENGTH_LONG).show()

        //TODO: Load the listing from DB
        //TODO: Do a Forward geocoding
        //TODO: Display all of the markers

    }

    //TODO: Implement on marker click.
    /*
    Depending on the situation, the following has to happen:
    If its to select the listing location, the marker updates the listing with the location, updates the user, and returns to the main activity.

    If it's displaying all of the listings or a particular listing, it should take the user to the display listing activity.

     */
}

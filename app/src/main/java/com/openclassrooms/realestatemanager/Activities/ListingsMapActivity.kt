package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.ACTIVITY_TASK
import com.openclassrooms.realestatemanager.Utilities.LISTING_ID
import com.openclassrooms.realestatemanager.Utilities.LOCATION_IQ_KEY
import com.openclassrooms.realestatemanager.Utilities.TASK_SELECT_LISTING_LOCATION
import com.openclassrooms.realestatemanager.database_files.Listing
import kotlinx.android.synthetic.main.activity_listings_map.*

class ListingsMapActivity : AppCompatActivity(), ListingGeocoder.OnConnectionResultListener {

    var incomingIntent: Intent? = null
    var activityTask: Int = 0
    var listingId: Long = 0
    val keyMap = HashMap<String, String>()

    lateinit var listingGeocoder: ListingGeocoder
    lateinit var listing: Listing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapquest_token))
        setContentView(R.layout.activity_listings_map)

        //Setup the ListingGeocoder object.
        keyMap.put(LOCATION_IQ_KEY, getString(R.string.locationIQ_token))
        val uriBuilder = Uri.Builder()
        listingGeocoder = ListingGeocoder(uriBuilder, this, keyMap)
        listingGeocoder.listener = this

        //Get the incoming Intent and deterline which task code needs to get done.
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

                //TODO: Task for displaying all listings.

                //TODO: Task for displaying all listings near the user.

                //TODO: Task for displaying a single listing centered on the map.
            }
        }
    }


    fun displayPossibleListingLocationMarkers(map: MapboxMap) {

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

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onConnectionResult(result: String) {


    }

    override fun onConnectionError(errorCode: Int) {

    }
}

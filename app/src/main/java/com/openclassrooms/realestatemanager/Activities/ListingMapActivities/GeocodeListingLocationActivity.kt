package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import com.openclassrooms.realestatemanager.Utilities.HelperMethods
import com.openclassrooms.realestatemanager.Utilities.LISTING_ID
import com.openclassrooms.realestatemanager.database_files.Listing
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GeocodeListingLocationActivity : ListingMapBaseActivity(), ListingGeocoder.OnConnectionResultListener {

    lateinit var listingGeocoder: ListingGeocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        incomingIntent = intent
        incomingIntent?.let {
            listingId = it.getLongExtra(LISTING_ID, 0)
        }

        val uriBuilder = Uri.Builder()
        listingGeocoder = ListingGeocoder(uriBuilder, this, keyMap)
    }

    override fun inializeActivity() {
        //super.inializeActivity()
        val mapReadyCallback = object : OnMapReadyCallback {
            override fun onMapReady(mapboxMap: MapboxMap) {
                map = mapboxMap
                mapboxMap.setStyle(Style.MAPBOX_STREETS)
            }
        }
        map_view.getMapAsync(mapReadyCallback)
        geocodeListing()
    }

    fun geocodeListing() {
        GlobalScope.launch {
            val futureListing = async { listingViewModel.getListingById(listingId) }
            val retrievedListing = futureListing.await()
            listing = retrievedListing
            val url = listingGeocoder.buildForwardGeocodingUrl(retrievedListing)
            listingGeocoder.getListingLocationSuspend(url)
        }
    }

    override fun onGeocodingResult(result: String) {
        val list = listingGeocoder.processListingLocationJsonResponse(result)
        val helperMethods = HelperMethods()

        runOnUiThread {
            helperMethods.placeListingLocationMarkers(map, list)
        }

    }

    override fun onGeocodingError(errorCode: Int) {
        runOnUiThread {
            Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
        }
    }

    fun updateListing(listing: Listing) {
        GlobalScope.launch {
            val id = async { listingViewModel.updateListing(listing) }.await()
            when (id) {
                1 -> {
                    runOnUiThread {
                        dialogBuilder
                                .buildSuccessDialogBuilder()
                                .setMessage("Listing Location updated!")
                                .show()
                    }

                }
                else -> {
                    runOnUiThread {
                        dialogBuilder
                                .buildErrorDialog()
                                .show()
                    }
                }

            }
        }

    }
}
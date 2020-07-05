package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.Utilities.LISTING_ID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SingleListingMapActivity : ListingMapBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        incomingIntent = intent
        incomingIntent?.let {
            listingId = it.getLongExtra(LISTING_ID, 0)
        }

        //Get the listing passed in from the intent.
        GlobalScope.launch {
            val futureListing = async { listingViewModel.getListingById(listingId) }
            val retrievedListing = futureListing.await()
            listing = retrievedListing

            runOnUiThread {
                initializeActivity()
            }
        }

    }

    override fun initializeActivity() {
        Toast.makeText(this, listing.id.toString(), Toast.LENGTH_LONG).show()
        val mapReadyCallback = object : OnMapReadyCallback {
            override fun onMapReady(mapboxMap: MapboxMap) {
                map = mapboxMap
                mapboxMap.setOnMarkerClickListener(markerClickListener)
                mapboxMap.setStyle(Style.MAPBOX_STREETS)
                helperMethods.placeListingMarker(map, listing)
            }
        }
        map_view.getMapAsync(mapReadyCallback)

    }

    val markerClickListener = object : MapboxMap.OnMarkerClickListener {
        override fun onMarkerClick(marker: Marker): Boolean {
            return true
        }
    }
}
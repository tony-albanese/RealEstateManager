package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.os.Bundle
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.Utilities.LISTING_ID

class SingleListingMapActivity : ListingMapBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        incomingIntent = intent
        incomingIntent?.let {
            listingId = it.getLongExtra(LISTING_ID, 0)
        }
    }

    override fun inializeActivity() {
        val mapReadyCallback = object : OnMapReadyCallback {
            override fun onMapReady(mapboxMap: MapboxMap) {
                map = mapboxMap
                mapboxMap.setOnMarkerClickListener(markerClickListener)
                mapboxMap.setStyle(Style.MAPBOX_STREETS)
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
package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import kotlinx.android.synthetic.main.activity_listings_map.*

class ListingMapBaseActivity : AppCompatActivity() {

    lateinit var listingViewModel: ListingViewModel
    lateinit var map: MapboxMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapquest_token))
        setContentView(R.layout.activity_listings_map)

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)
        map_view?.getMapAsync(mapReadyCallback)

    }

    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(mapboxMap: MapboxMap) {
            map = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS)

        }
    }
}
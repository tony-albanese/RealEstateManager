package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.Activities.MainActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.LOCATION_IQ_KEY
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel

open class ListingMapBaseActivity : AppCompatActivity() {

    lateinit var listingViewModel: ListingViewModel
    lateinit var listing: Listing
    lateinit var map: MapboxMap
    lateinit var map_view: MapView
    var listingId: Long = 0
    val keyMap = HashMap<String, String>()

    var incomingIntent: Intent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapquest_token))
        setContentView(R.layout.activity_listings_map)

        keyMap.put(LOCATION_IQ_KEY, getString(R.string.locationIQ_token))
        listing = Listing()
        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)
        map_view = findViewById<MapView>(R.id.map_view)
        inializeActivity()

    }

    open fun inializeActivity() {
        val mapReadyCallback = object : OnMapReadyCallback {
            override fun onMapReady(mapboxMap: MapboxMap) {
                map = mapboxMap
                mapboxMap.setStyle(Style.MAPBOX_STREETS)
            }
        }
        map_view.getMapAsync(mapReadyCallback)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
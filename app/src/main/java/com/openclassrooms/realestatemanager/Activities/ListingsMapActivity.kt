package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_listings_map.*

class ListingsMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapquest_token))
        setContentView(R.layout.activity_listings_map)

        map_view?.getMapAsync(mapReadyCallback)
    }

    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(mapboxMap: MapboxMap) {
            mapboxMap.setStyle(Style.MAPBOX_STREETS)
        }

    }

}

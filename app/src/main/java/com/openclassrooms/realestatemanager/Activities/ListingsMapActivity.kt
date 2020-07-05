package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.*
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import kotlinx.android.synthetic.main.activity_listings_map.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListingsMapActivity : AppCompatActivity(), ListingGeocoder.OnConnectionResultListener {

    var incomingIntent: Intent? = null
    var activityTask: Int = 0
    var listingId: Long = 0
    val keyMap = HashMap<String, String>()

    lateinit var listingGeocoder: ListingGeocoder
    lateinit var listing: Listing
    lateinit var listingViewModel: ListingViewModel
    lateinit var map: MapboxMap
    lateinit var dialogBuilder: CustomDialogBuilder
    lateinit var helperMethods: HelperMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapquest_token))
        setContentView(R.layout.activity_listings_map)

        listing = Listing()
        dialogBuilder = CustomDialogBuilder(this)
        helperMethods = HelperMethods()

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

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)

        map_view?.getMapAsync(mapReadyCallback)
    }

    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(mapboxMap: MapboxMap) {
            mapboxMap.setOnMarkerClickListener(markerClickListener)
            map = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS)

            when (activityTask) {
                TASK_SELECT_LISTING_LOCATION -> {
                    geocodeListing()
                }

                //TODO: Task for displaying all listings.

                //TODO: Task for displaying all listings near the user.

                //TODO: Task for displaying a single listing centered on the map.
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

    fun geocodeListing() {
        GlobalScope.launch {
            val futureListing = async { listingViewModel.getListingById(listingId) }
            val retrievedListing = futureListing.await()
            listing = retrievedListing
            val url = listingGeocoder.buildForwardGeocodingUrl(retrievedListing)
            listingGeocoder.getListingLocationSuspend(url)
        }
    }

    val markerClickListener = object : MapboxMap.OnMarkerClickListener {
        override fun onMarkerClick(marker: Marker): Boolean {
            when (activityTask) {
                TASK_SELECT_LISTING_LOCATION -> {
                    listing.listingLocation = marker.position
                    listing.listingImageUrl = helperMethods.buildStaticImageUrl(
                            getString(R.string.mapquest_token),
                            listing
                    )
                    updateListing(listing)
                }
            }
            return true
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
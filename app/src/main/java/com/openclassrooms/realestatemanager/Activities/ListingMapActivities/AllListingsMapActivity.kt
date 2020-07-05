package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.openclassrooms.realestatemanager.R

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
class AllListingsMapActivity : ListingMapBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.initializeActivity()
        requestForegroundPermission()
    }

    private fun isForegroundPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermission() {

        val provideRationale = isForegroundPermissionGranted()
        val mapLayout = findViewById<ConstraintLayout>(R.id.map_activity_layout)

        if (provideRationale) {
            Snackbar.make(
                    mapLayout,
                    "You need to grant permission to see the listings near you",
                    Snackbar.LENGTH_LONG
            )
                    .setAction("OK") {
                        ActivityCompat.requestPermissions(this@AllListingsMapActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                        )
                    }
                    .show()
        } else {
            ActivityCompat.requestPermissions(
                    this@AllListingsMapActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {

                grantResults.isEmpty() -> Toast.makeText(this, "Action cancelled", Toast.LENGTH_LONG).show()

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    //TODO Load all of the listings and display them. Center the camera on the user.
                    centerMapOnUser()
                }

                else -> {
                    //TODO Load all of the listings. Center the camera in the middle of all the listings.

                }

            }
        }
    }


    @SuppressLint("MissingPermission")
    fun centerMapOnUser() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener {
            val currentLocation = LatLng(it.latitude, it.longitude)
            val position = CameraPosition.Builder()
                    .target(currentLocation)
                    .zoom(12.toDouble())
                    .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1500)
        }

    }

}
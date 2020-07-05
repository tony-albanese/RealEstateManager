package com.openclassrooms.realestatemanager.Activities.ListingMapActivities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
class AllListingsMapActivity : ListingMapBaseActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inializeActivity()
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
}
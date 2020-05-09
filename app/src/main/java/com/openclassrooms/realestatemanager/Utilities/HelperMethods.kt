package com.openclassrooms.realestatemanager.Utilities

import android.content.Context
import android.content.Intent
import android.view.Menu
import com.mapbox.api.staticmap.v1.MapboxStaticMap
import com.mapbox.api.staticmap.v1.StaticMapCriteria
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.openclassrooms.realestatemanager.Activities.EditListingActivity
import com.openclassrooms.realestatemanager.Constants.LISTING_ID_KEY
import com.openclassrooms.realestatemanager.Geolocation.GeocodingModel.ForwardGeocodeResponse
import com.openclassrooms.realestatemanager.database_files.Listing


class HelperMethods() {

    fun onUnpublishedListingClick(context: Context, unpublishedListings: List<Listing>, menuId: Long) {
        val listingId = unpublishedListings.filter {
            it.id == menuId
        }
        if (listingId.size == 1) {
            val intent = Intent(context, EditListingActivity::class.java)
            intent.putExtra(LISTING_ID_KEY, menuId)
            context.startActivity(intent)
        }
    }

    fun generateUnpublishedListingMenu(menu: Menu?, menuItemPosition: Int, unpublishedListings: List<Listing>) {
        val subMenu = menu?.getItem(menuItemPosition)?.subMenu.apply {
            for (listing in unpublishedListings) {
                this?.add(Menu.NONE, listing.id.toInt(), Menu.NONE, listing.listingStreetAddress)
            }
        }
    }

    fun onAddNewListingClick(context: Context) {
        val intent = Intent(context, EditListingActivity::class.java)
        intent.putExtra(LISTING_ID_KEY, 0.toLong())
        context.startActivity(intent)
    }

    fun onEditListingClick(context: Context, id: Long) {
        val intent = Intent(context, EditListingActivity::class.java)
        intent.putExtra(LISTING_ID_KEY, id)
        context.startActivity(intent)
    }

    @Suppress("DEPRECATION")
    fun placeListingLocationMarkers(map: MapboxMap, list: ArrayList<ForwardGeocodeResponse>) {
        for (location in list) {
            ConversionUtilities.setGeocodeLatLng(location)
            map.addMarker(MarkerOptions()
                    .position(location.latLng)
            )

        }

        val position = CameraPosition.Builder()
                .target(list.get(0).latLng)
                .zoom(12.toDouble())
                .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1500)

    }

    fun buildStaticImageUrl(token: String, listing: Listing): String {
        return MapboxStaticMap.builder()
                .accessToken(token)
                .styleId(StaticMapCriteria.LIGHT_STYLE)
                .cameraPoint(Point.fromLngLat(
                        listing.listingLocation?.longitude!!,
                        listing.listingLocation?.latitude!!))
                .cameraZoom(13.toDouble())
                .build()
                .url()
                .toString()
    }
}
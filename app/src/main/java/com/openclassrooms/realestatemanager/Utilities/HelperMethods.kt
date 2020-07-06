package com.openclassrooms.realestatemanager.Utilities

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mapbox.api.staticmap.v1.MapboxStaticMap
import com.mapbox.api.staticmap.v1.StaticMapCriteria
import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.openclassrooms.realestatemanager.Activities.EditListingActivity
import com.openclassrooms.realestatemanager.Constants.LISTING_ID_KEY
import com.openclassrooms.realestatemanager.Geolocation.GeocodingModel.ForwardGeocodeResponse
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.Listing
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


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

    @Suppress("DEPRECATION")
    fun placeListingMarker(map: MapboxMap, listing: Listing) {
        map.addMarker(
                MarkerOptions()
                        .position(listing.listingLocation)
        )

        val position = CameraPosition.Builder()
                .target(listing.listingLocation)
                .zoom(12.toDouble())
                .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1500)
    }

    @Suppress("DEPRECATION")
    fun placeListingMarkersOnMap(map: MapboxMap, list: List<Listing>, hashMap: HashMap<Long, Listing>) {
        for (listing in list) {

            listing.listingLocation?.apply {
                map.addMarker(
                        MarkerOptions()
                                .position(this)
                ).apply {
                    hashMap.put(this.id, listing)
                }

            }

        }

    }

    fun buildStaticImageUrl(token: String, listing: Listing): String {

        val point = Point.fromLngLat(listing.listingLocation?.longitude!!, listing.listingLocation?.latitude!!)
        val annotation = StaticMarkerAnnotation
                .builder()
                .lnglat(point)
                .build()
        val list = mutableListOf<StaticMarkerAnnotation>()
        list.add(annotation)
        return MapboxStaticMap.builder()
                .accessToken(token)
                .styleId(StaticMapCriteria.LIGHT_STYLE)
                .staticMarkerAnnotations(list)
                .cameraPoint(Point.fromLngLat(
                        listing.listingLocation?.longitude!!,
                        listing.listingLocation?.latitude!!))
                .cameraZoom(13.toDouble())
                .build()
                .url()
                .toString()
    }

    suspend fun saveStaticImage(urlString: String): Bitmap? {

        var connection: HttpURLConnection? = null
        try {
            val url: URL? = URL(urlString)
            connection = url?.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream = connection?.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)

            val bmp = BitmapFactory.decodeStream(bufferedInputStream)
            return bmp
        } catch (excepetion: IOException) {

            return null
        } catch (exception: MalformedURLException) {

            return null
        } catch (exception: Exception) {
            return null
        } finally {
            connection?.disconnect()
        }
    }

    fun calculateCentralLocation(list: ArrayList<Listing>): LatLng {

        var latSum: Double = 0.0
        var longSum: Double = 0.0
        var numberOfPoints = 0
        for (listing in list) {
            listing.listingLocation?.apply {
                latSum += this.latitude
                longSum += this.longitude
                numberOfPoints++
            }

        }

        val averageLatitude = (latSum / numberOfPoints)
        val averageLongitude = (longSum / numberOfPoints)

        return LatLng(averageLatitude, averageLongitude)

    }

    //TODO () Add data from listing.
    fun createListingDetailPopup(context: Context, anchorView: View, listing: Listing?) {

        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupContentView: View = layoutInflater.inflate(R.layout.popup_layout, null)
        val popupWindow = PopupWindow(popupContentView, 400, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)


        popupContentView.findViewById<TextView>(R.id.pop_tv_price).setText("$200322")
        popupContentView.findViewById<TextView>(R.id.pop_tv_address).setText("This is the address.")
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
        popupWindow.update(0, 0, popupWindow.getWidth(), popupWindow.getHeight())
        popupWindow.showAsDropDown(anchorView)

        val closeButton = popupContentView.findViewById<Button>(R.id.pop_btn_close)
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }
}
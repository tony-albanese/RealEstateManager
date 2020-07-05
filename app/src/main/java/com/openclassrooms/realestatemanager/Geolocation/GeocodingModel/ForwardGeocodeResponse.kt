package com.openclassrooms.realestatemanager.Geolocation.GeocodingModel

import com.google.gson.annotations.SerializedName
import com.mapbox.mapboxsdk.geometry.LatLng

data class ForwardGeocodeResponse(

        @field:SerializedName("licence")
        val licence: String? = null,

        @field:SerializedName("boundingbox")
        val boundingbox: List<String?>? = null,

        @field:SerializedName("importance")
        val importance: Double? = null,

        @field:SerializedName("lon")
        val lon: String? = null,

        @field:SerializedName("display_name")
        val displayName: String? = null,

        @field:SerializedName("place_id")
        val placeId: String? = null,

        @field:SerializedName("lat")
        val lat: String? = null,

        var latLng: LatLng? = null
)

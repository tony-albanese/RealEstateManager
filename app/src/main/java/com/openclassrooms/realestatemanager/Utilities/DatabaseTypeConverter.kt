package com.openclassrooms.realestatemanager.Utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.mapboxsdk.geometry.LatLng

class DatabaseTypeConverter {
    @TypeConverter
    fun fromLatLng(latLng: LatLng): String {
        return Gson().toJson(latLng)
    }

    @TypeConverter
    fun latLngFromString(latLngJson: String): LatLng {
        val type = object : TypeToken<LatLng>() {}.type
        return Gson().fromJson<LatLng>(latLngJson, type)
    }
}
package com.openclassrooms.realestatemanager.Utilities

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.mapboxsdk.geometry.LatLng
import java.util.*

class DatabaseTypeConverter {
    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String {
        if (latLng == null) {
            return ""
        } else {
            return Gson().toJson(latLng)
        }

    }

    @TypeConverter
    fun latLngFromString(latLngJson: String?): LatLng? {
        if (latLngJson.isNullOrEmpty()) {
            return null
        } else {
            val type = object : TypeToken<LatLng>() {}.type
            return Gson().fromJson<LatLng>(latLngJson, type)
        }

    }

    @TypeConverter
    fun uriToString(uri: Uri?): String {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(uriString: String): Uri? {
        return Uri.parse(uriString)
    }

    @TypeConverter
    fun uuidToString(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun stringToUuid(uuidString: String): UUID {
        return UUID.fromString(uuidString)
    }
}
package com.openclassrooms.realestatemanager.Utilities

import com.mapbox.mapboxsdk.geometry.LatLng
import com.openclassrooms.realestatemanager.Geolocation.GeocodingModel.ForwardGeocodeResponse
import java.text.NumberFormat
import java.util.*

object ConversionUtilities {

    fun formatCurrencyIntToString(amount: Int, locale: Locale): String {
        val value = locale.let { locale: Locale ->
            NumberFormat.getCurrencyInstance(locale).apply {
                this.maximumFractionDigits = 0
                currency = Currency.getInstance("USD")
            }
        }
        return value.format(amount.toLong())
    }

    fun formatCurrencyToInteger(amount: String): Int {
        val convertedAmount = amount.filter {
            it.isDigit()
        }
        return convertedAmount.toInt()
    }

    fun reformatCurrencyString(unformattedCurrencyString: String, locale: Locale): String {
        val newDigits = this.formatCurrencyToInteger(unformattedCurrencyString)
        return this.formatCurrencyIntToString(newDigits, locale)
    }

    fun setGeocodeLatLng(response: ForwardGeocodeResponse) {
        try {
            val lat = response.lat?.toDouble()
            val long = response.lon?.toDouble()

            if (lat != null && long != null) {
                response.latLng = LatLng(lat, long)
            }
        } catch (e: NumberFormatException) {

        }
    }
}
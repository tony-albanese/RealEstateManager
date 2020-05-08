package com.openclassrooms.realestatemanager.Geolocation

import android.content.Context
import android.net.Uri
import com.openclassrooms.realestatemanager.Utilities.LOCATION_IQ_KEY
import com.openclassrooms.realestatemanager.database_files.Listing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ListingGeocoder(val uriBuilder: Uri.Builder, val context: Context, keyMap: HashMap<String, String>) {

    var searchUrl = ""
    var geocodingBaseUrl: String

    val SCHEME: String = "https"
    val GEOCODING_AUTHORITY: String = "eu1.locationiq.com"
    val GEOCODING_PATH: String = "v1/search.php"

    init {
        geocodingBaseUrl = buildForwardGeocodingBaseUrl(keyMap.get(LOCATION_IQ_KEY)!!)
    }


    interface OnConnectionResultListener {
        fun onConnectionResult(result: String)
        fun onConnectionError(errorCode: Int)
    }

    suspend fun make(url: String) {
        val connection = connectToSite(stringToUrl(url))
        if (connection == null) {
        } else {
            val result = readDataFromConnection(connection)
            when (result) {

            }
        }

    }

    suspend fun readDataFromConnection(connection: HttpURLConnection?): String {
        return readDataFromConnection(connection)
    }
    
    /*
   Accepts a search url as input and returns a response from the server. The default
   search url is the one created by the constructor.
    */
    fun retrieveServerResponse(url: String = searchUrl): String = runBlocking {

        val connection: HttpURLConnection? =
                connectToSite(stringToUrl(url))
        val result = withContext(Dispatchers.IO) {
            readDataFromConnection(connection!!)
        }
        return@runBlocking result
    }

    /**
     * Converts a given string to URL
     */
    private fun stringToUrl(urlString: String): URL? {
        val url: URL
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
            return null
        }
        return url
    }

    /**
     * Accepts a URL and returns an HTTP connection.
     */
    private fun connectToSite(url: URL?): HttpURLConnection? {
        try {
            return url?.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            return null
        }
    }

    /**
     * Reads data from the connection and returns the response as a string.
     */
    private fun readDataFromConnection(connection: HttpURLConnection): String {
        val stringBuilder = StringBuilder()
        try {
            connection.connect()
            val inStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inStream) as Reader?)

            while (true) {
                var line: String? = reader.readLine() ?: break
                stringBuilder.append(line)
            }

            return stringBuilder.toString()
        } catch (e: Exception) {
            return "ERROR"
        }
    }

    /*
    This method builds the forward gecoding base url.
     */
    fun buildForwardGeocodingBaseUrl(key: String): String {
        return uriBuilder.scheme(SCHEME)
                .authority(GEOCODING_AUTHORITY)
                .path(GEOCODING_PATH)
                .appendQueryParameter("key", key)
                .toString()
    }

    /*
    Method that takes a listing and builds the forward geocoding url frome the passed in listing address.
     */
    fun buildForwardGeocodingUrl(listing: Listing): String {
        val streetParameter = "street"
        val cityParameter = "city"
        val postalCodeParameter = "postalcode"
        val formatParameter = "format"
        return Uri.parse(geocodingBaseUrl)
                .buildUpon()
                .appendQueryParameter(streetParameter, listing.listingStreetAddress)
                .appendQueryParameter(cityParameter, listing.listingCity)
                .appendQueryParameter(postalCodeParameter, listing.listingZipCode).appendQueryParameter(formatParameter, "json")
                .build()
                .toString()
    }
}
package com.openclassrooms.realestatemanager.Geolocation

import android.util.Log
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

class ListingGeocoder {

    var searchUrl = ""

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
            Log.e("Connect error.", e.toString())
            return "ERROR"
        }
    }
}
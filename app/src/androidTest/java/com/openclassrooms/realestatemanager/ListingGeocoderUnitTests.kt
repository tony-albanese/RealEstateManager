package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.Uri
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListingGeocoderUnitTests {

    val uriBuilder = Uri.Builder()
    val FAKE_KEY = "FAKEKEY"


    lateinit var mockContext: Context

    @Before
    fun setup() {
        //MockitoAnnotations.initMocks(this)
        mockContext = MockContext()
    }

    @Test
    fun testGeocodingBaseUriBuilder() {

        val expectedUrl = ""
        `when`(mockContext.getString(R.string.locationIQ_token))
                .thenReturn(FAKE_KEY)
        val listingGeocoder = ListingGeocoder(uriBuilder, mockContext)
        val testUrl = listingGeocoder.geocodingBaseUrl
        System.out.println(testUrl)
        // assertEquals()

    }
}
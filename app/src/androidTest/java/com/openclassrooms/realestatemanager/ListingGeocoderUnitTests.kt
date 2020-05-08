package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import com.openclassrooms.realestatemanager.Utilities.LOCATION_IQ_KEY
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListingGeocoderUnitTests {

    val uriBuilder = Uri.Builder()
    val FAKE_KEY = "FAKEKEY"


    @Mock
    private lateinit var mockContext: Context

    lateinit var context: Context

    @Before
    fun setup() {
        context = getInstrumentation().targetContext
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGeocodingBaseUriBuilder() {


        val expectedUrl = "https://eu1.locationiq.com/v1/search.php?key=FAKEKEY"
        val map = HashMap<String, String>()
        map.put(LOCATION_IQ_KEY, FAKE_KEY)

        val listingGeocoder = ListingGeocoder(uriBuilder, context, map)

        val testUrl = listingGeocoder.buildForwardGeocodingBaseUrl(map.get(LOCATION_IQ_KEY)!!)
        assert(testUrl.equals(expectedUrl))

    }
}
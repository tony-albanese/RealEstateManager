package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import com.openclassrooms.realestatemanager.Utilities.LOCATION_IQ_KEY
import com.openclassrooms.realestatemanager.database_files.Listing
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListingGeocoderUnitTests {

    val FAKE_KEY = "FAKEKEY"

    @Mock
    private lateinit var mockContext: Context

    val uriBuilder = Uri.Builder()
    val map = HashMap<String, String>()

    lateinit var context: Context
    lateinit var testListing: Listing
    lateinit var listingGeocoder: ListingGeocoder

    @Before
    fun setup() {
        context = getInstrumentation().targetContext
        MockitoAnnotations.initMocks(this)
        testListing = Listing()

        map.put(LOCATION_IQ_KEY, FAKE_KEY)
        listingGeocoder = ListingGeocoder(uriBuilder, context, map)
    }


    @Test
    fun testGeocodingBaseUriBuilder() {

        val expectedUrl = "https://eu1.locationiq.com/v1/search.php?key=FAKEKEY"


        val testUrl = listingGeocoder.buildForwardGeocodingBaseUrl(map.get(LOCATION_IQ_KEY)!!)
        assert(testUrl.equals(expectedUrl))

    }

    @Test
    fun testLocationGeocodingUrlBuilder() {

        testListing.listingStreetAddress = "5414 S. Neenah"
        testListing.listingZipCode = "60638"
        testListing.listingCity = "Chicago"

        val expectedUrl = "https://eu1.locationiq.com/v1/search.php?key=FAKEKEY&street=5414%20S.%20Neenah&city=Chicago&postalcode=60638&format=json"
        val testUrl = listingGeocoder.buildForwardGeocodingUrl(testListing)
        assert(testUrl.equals(expectedUrl))
    }
}
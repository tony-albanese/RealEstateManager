package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.openclassrooms.realestatemanager.Geolocation.GeocodingModel.ForwardGeocodeResponse
import com.openclassrooms.realestatemanager.Geolocation.ListingGeocoder
import com.openclassrooms.realestatemanager.Utilities.ConversionUtilities
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

    @Test
    fun testGeocoderResponseProcess() {

        val resultList = listingGeocoder.processListingLocationJsonResponse(sampleForwardGeocodingResponse)

        assert(resultList.size == 10)
    }

    @Test
    fun testGeocoderWithSingleResponseItem() {
        var resultsList = listingGeocoder.processListingLocationJsonResponse(
                sampleForwardGeocodingResponseSingleResult
        )
        assert(resultsList.size == 1)
    }

    @Test
    fun testGeocoderWithErrorResponseItem() {
        var resultsList = listingGeocoder.processListingLocationJsonResponse(
                errorResponseUnableToGeocode
        )
        assert(resultsList.size == 0)
    }

    @Test
    fun testGeocoderWithInvalidKey() {
        var resultsList = listingGeocoder.processListingLocationJsonResponse(
                errorResponseInvalidKey
        )
        assert(resultsList.size == 0)
    }

    @Test
    fun testConvertStringToLatLng() {
        var list = listingGeocoder.processListingLocationJsonResponse(
                sampleForwardGeocodingResponseSingleResult
        )

        for (response in list) {
            ConversionUtilities.setGeocodeLatLng(response)
        }

        val response = list.get(0)
        assert(response.latLng != null)
    }

    @Test
    fun testLatLngConversionWithNullValues() {
        val testResponse = ForwardGeocodeResponse()
        ConversionUtilities.setGeocodeLatLng(testResponse)
        assert(testResponse.latLng == null)
    }

    //region TestResponseStrings
    val errorResponseUnableToGeocode = "{\"error\":\"Unable to geocode\"}"
    val errorResponseInvalidKey = "{\"error\":\"Inavlid key\"}"
    val sampleForwardGeocodingResponse = "[{\"place_id\":\"334254683297\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"41.793587\",\"lon\":\"-87.785867\",\"display_name\":\"5418, S Neenah Ave, Garfield Ridge, Chicago, Cook County, Illinois, 60638, USA\",\"boundingbox\":[\"41.793587\",\"41.793587\",\"-87.785867\",\"-87.785867\"],\"importance\":0.25},{\"place_id\":\"330587374771\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"41.793601\",\"lon\":\"-87.785804\",\"display_name\":\"5418, South Neenah Avenue, Garfield Ridge, Chicago, Cook County, Illinois, 60638, USA\",\"boundingbox\":[\"41.793601\",\"41.793601\",\"-87.785804\",\"-87.785804\"],\"importance\":0.25},{\"place_id\":\"332038489233\",\"osm_type\":\"node\",\"osm_id\":\"353895965\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"44.11943\",\"lon\":\"-88.473444\",\"display_name\":\"WROE-FM (Neenah-Menasha), Vinland, Winnebago County, Wisconsin, 54956, USA\",\"boundingbox\":[\"44.11943\",\"44.11943\",\"-88.473444\",\"-88.473444\"],\"importance\":0.25},{\"place_id\":\"331525703575\",\"osm_type\":\"node\",\"osm_id\":\"353893392\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"44.254485\",\"lon\":\"-88.436967\",\"display_name\":\"WEMI-FM (Neenah-Menasha), Appleton, Outagamie County, Wisconsin, 54914, USA\",\"boundingbox\":[\"44.254485\",\"44.254485\",\"-88.436967\",\"-88.436967\"],\"importance\":0.25},{\"place_id\":\"331295161250\",\"osm_type\":\"node\",\"osm_id\":\"353893328\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"44.109985\",\"lon\":\"-88.505112\",\"display_name\":\"WNAM-AM (Neenah-Menasha), Vinland, Winnebago County, Wisconsin, 54901, USA\",\"boundingbox\":[\"44.109985\",\"44.109985\",\"-88.505112\",\"-88.505112\"],\"importance\":0.25},{\"place_id\":\"332671867637\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"44.167546\",\"lon\":\"-88.471624\",\"display_name\":\"Neenah, Winnebago County, Wisconsin, USA\",\"boundingbox\":[\"44.1344\",\"44.194612\",\"-88.523616\",\"-88.43407\"],\"importance\":0.25},{\"place_id\":\"332384675141\",\"osm_type\":\"node\",\"osm_id\":\"353898135\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"44.159986\",\"lon\":\"-88.465943\",\"display_name\":\"WNAM-AM (Neenah-Menasha), Neenah, Winnebago County, Wisconsin, 54956, USA\",\"boundingbox\":[\"44.159986\",\"44.159986\",\"-88.465943\",\"-88.465943\"],\"importance\":0.25},{\"place_id\":\"330353223731\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"36.639403\",\"lon\":\"-87.381463\",\"display_name\":\"NEENA CT 3554, Neena Ct, Clarksville, Montgomery County, Tennessee, 37042, USA\",\"boundingbox\":[\"36.639403\",\"36.639403\",\"-87.381463\",\"-87.381463\"],\"importance\":0.25},{\"place_id\":\"333090084665\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"36.638778\",\"lon\":\"-87.380879\",\"display_name\":\"NEENA CT 3563, Neena Ct, Clarksville, Montgomery County, Tennessee, 37042, USA\",\"boundingbox\":[\"36.638778\",\"36.638778\",\"-87.380879\",\"-87.380879\"],\"importance\":0.25},{\"place_id\":\"333915695588\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"lat\":\"36.639198\",\"lon\":\"-87.381568\",\"display_name\":\"NEENA CT 3558, Neena Ct, Clarksville, Montgomery County, Tennessee, 37042, USA\",\"boundingbox\":[\"36.639198\",\"36.639198\",\"-87.381568\",\"-87.381568\"],\"importance\":0.25}]"

    val sampleForwardGeocodingResponseSingleResult = "[{\"place_id\":\"125192230\",\"licence\":\"https:\\/\\/locationiq.com\\/attribution\",\"osm_type\":\"way\",\"osm_id\":\"162928710\",\"boundingbox\":[\"41.7937999\",\"41.7938703\",\"-87.7858807\",\"-87.785685\"],\"lat\":\"41.7938351\",\"lon\":\"-87.78578285\",\"display_name\":\"5414, South Neenah Avenue, Clearing, Clearing, Forest View, Cook County, Illinois, 60638, USA\",\"class\":\"building\",\"type\":\"yes\",\"importance\":0.421}]"
    //endregion
}
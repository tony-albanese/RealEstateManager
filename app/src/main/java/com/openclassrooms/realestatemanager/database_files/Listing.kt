package com.openclassrooms.realestatemanager.database_files

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.mapboxsdk.geometry.LatLng

@Entity(tableName = "table_listings")
data class Listing(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = "listing_description") var listingDescription: String = "",
        @ColumnInfo(name = "listing_type") var listingType: String = "",
        @ColumnInfo(name = "listing_street_address") var listingStreetAddress: String = "",
        @ColumnInfo(name = "listing_city") var listingCity: String = "",
        @ColumnInfo(name = "listing_zip_code") var listingZipCode: String = "",
        @ColumnInfo(name = "listing_area") var listingArea: Int = 0,
        @ColumnInfo(name = "listing_price") var listingPrice: Int = 0,
        @ColumnInfo(name = "total_rooms") var numberOfRooms: Int = 0,
        @ColumnInfo(name = "number_bedrooms") var numberOfBedrooms: Int = 0,
        @ColumnInfo(name = "number_bathrooms") var numberBathrooms: Double = 0.0,
        @ColumnInfo(name = "isAvailable") var isAvailable: Boolean = true,
        @ColumnInfo(name = "listing_date") var listingDate: String = "",
        @ColumnInfo(name = "listing_sale_date") var listingSaleDate: String = "",
        @ColumnInfo(name = "listing_agent_id") var listingAgentId: Long = 0,
        @ColumnInfo(name = "listing_agent_name") var listingAgentName: String = "",
        @ColumnInfo(name = "listing_is_published") var listingIsPublished: Boolean = false,

        //TODO: Still need a way to record POIs.
        //TODO: Still need to geocode.
        //TODO: Need to turn this into a column.
        var latLng: LatLng? = null
) {
}
package com.openclassrooms.realestatemanager.DisplayListings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.ConversionUtilities
import com.openclassrooms.realestatemanager.database_files.Listing
import java.util.*

class ListingAdapter(val locale: Locale, val callback: (Listing) -> Unit) : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    private var listings = emptyList<Listing>()
    var previousView: View? = null
    var previousPosition = -1
    var selectedPosition = -1
    var selectedView: View? = null

    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listingItemType = itemView.findViewById<MaterialTextView>(R.id.tv_listing_item_listing_type)
        val listingItemCity = itemView.findViewById<MaterialTextView>(R.id.tv_listing_item_listing_city)
        val listingItemPrice = itemView.findViewById<MaterialTextView>(R.id.tv_listing_item_listing_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listing_item_layout, parent, false)
        return ListingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val currentListing = listings.get(position)

        holder.listingItemCity.text = currentListing.listingCity
        holder.listingItemType.text = currentListing.listingType
        holder.listingItemPrice.text = ConversionUtilities.formatCurrencyIntToString(currentListing.listingPrice, locale)

        holder.itemView.setOnClickListener {
            //TODO: Set the style instead of the individual properties.
            //TODO: Clean up the if statement.
            selectedPosition = position
            if (selectedPosition == previousPosition) {

            } else if (selectedPosition != previousPosition) {
                previousPosition = selectedPosition
                selectedPosition = position
                previousView = selectedView
                selectedView = holder.itemView
                previousView?.setBackgroundColor(Color.TRANSPARENT)
                selectedView?.setBackgroundColor(Color.BLUE)
            }



            callback(currentListing)
        }

    }

    internal fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }
}
package com.openclassrooms.realestatemanager.DisplayListings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.ConversionUtilities
import com.openclassrooms.realestatemanager.database_files.Listing
import java.util.*

class ListingAdapter(val locale: Locale) : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    private var listings = emptyList<Listing>()
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
    }

    internal fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }
}
package com.openclassrooms.realestatemanager.recycler_selection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.Listing

class ListingAdapter : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    private var listings = emptyList<Listing>()

    init {
        setHasStableIds(true)
    }

    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listingItemView = itemView.findViewById<TextView>(R.id.tv_listing_text)

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listing_item_layout, parent, false)
        return ListingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    internal fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val current = listings[position]
        holder.listingItemView.text = current.listingDescription
    }
}
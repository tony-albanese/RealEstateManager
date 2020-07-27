package com.openclassrooms.realestatemanager.DisplayListings

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.openclassrooms.realestatemanager.Activities.DisplayListingPortaitActivity
import com.openclassrooms.realestatemanager.ListingPhotos.GlobalVariableApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.ConversionUtilities
import com.openclassrooms.realestatemanager.database_files.Listing
import kotlinx.android.synthetic.main.listing_item_layout.view.*
import java.util.*

class ListingAdapter(val locale: Locale, val isLandscape: Boolean, val globalVariabales: GlobalVariableApplication, val callback: (Listing) -> Unit) : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    private var listings = emptyList<Listing>()
    var previousView: View? = null
    var previousPosition = -1
    var selectedPosition = -1
    var selectedView: View? = null
    var initialSelectionCallack: InitialSelection? = null

    var initialSelectionInitializedFlag: Boolean = false

    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listingItemType = itemView.findViewById<MaterialTextView>(R.id.tv_listing_item_listing_type)
        val listingItemCity = itemView.findViewById<MaterialTextView>(R.id.tv_listing_item_listing_city)
        val listingItemPrice = itemView.findViewById<MaterialTextView>(R.id.tv_listing_item_listing_price)
        val listingHomeImageView = itemView.findViewById<ImageView>(R.id.iv_listing_item_image)
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
        val context = holder.itemView.context

        holder.listingItemCity.text = currentListing.listingCity
        holder.listingItemType.text = currentListing.listingType
        holder.listingItemPrice.text = ConversionUtilities.formatCurrencyIntToString(currentListing.listingPrice, locale)

        Glide.with(holder.itemView.context)
                .load(currentListing.listingMainPhotoUri)
                .placeholder(holder.itemView.context.resources.getDrawable(R.drawable.placeholder_image))
                .error(holder.itemView.context.resources.getDrawable(R.drawable.placeholder_image))
                .into(holder.listingHomeImageView)

        holder.itemView.setOnClickListener {
            if (isLandscape) {
                selectedPosition = position
                if (selectedPosition != previousPosition) {
                    previousPosition = selectedPosition
                    selectedPosition = position
                    previousView = selectedView
                    selectedView = holder.itemView

                    //Set the properties of the previous view.
                    previousView?.setBackgroundColor(Color.TRANSPARENT)
                    previousView?.tv_listing_item_listing_price
                            ?.setTextColor(context.resources.getColor(R.color.colorAccent))

                    //Set the properties of the selected view.
                    selectedView?.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
                    selectedView?.tv_listing_item_listing_price
                            ?.setTextColor(context.resources.getColor(R.color.white))

                }
            } else {
                val context = holder.itemView.context
                val intent = Intent(context, DisplayListingPortaitActivity::class.java)
                intent.putExtra("LISTING_ID", currentListing.id)
                context.startActivity(intent)
            }

            callback(currentListing)
        }

        if (position == globalVariabales.selectedPosition && isLandscape && !initialSelectionInitializedFlag) {
            this.initialSelectionCallack?.initializeInitialSelection(holder.itemView, position, currentListing)
        }

    }

    internal fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }

    interface InitialSelection {
        fun initializeInitialSelection(itemView: View, position: Int, listing: Listing)
    }
}
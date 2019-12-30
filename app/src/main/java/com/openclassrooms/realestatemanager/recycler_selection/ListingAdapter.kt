package com.openclassrooms.realestatemanager.recycler_selection

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.CUSTOM_TAG
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.Listing

class ListingAdapter : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    private var listings = emptyList<Listing>()
    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listingItemView = itemView.findViewById<TextView>(R.id.tv_listing_text)

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int {
                        Log.i(CUSTOM_TAG, "get Item details called (position)")
                        return adapterPosition
                    }

                    override fun getSelectionKey(): Long? {
                        Log.i(CUSTOM_TAG, "get Item details called (itemId)")
                        return itemId
                    }
                }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listing_item_layout, parent, false)
        return ListingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun getItemId(position: Int): Long {
        Log.i(CUSTOM_TAG, "getItemId called")
        return position.toLong()
    }

    internal fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        Log.i(CUSTOM_TAG, "onBindViewHolde called")
        val current = listings[position]
        holder.listingItemView.text = current.listingDescription

        val parent = holder.listingItemView.parent as ConstraintLayout


        //TODO: Add a null check on tracker.
        if (tracker!!.isSelected(position.toLong())) {
            Log.i(CUSTOM_TAG, "tracker selected")
            parent.background = ColorDrawable(
                    Color.parseColor("#80deea")
            )
        } else {
            parent.background = ColorDrawable(Color.WHITE)
            Log.i(CUSTOM_TAG, "tracker not selected")
        }


    }

    fun setTracker(tracker: SelectionTracker<Long>?) {
        Log.i(CUSTOM_TAG, "set tracker called)")
        this.tracker = tracker
    }
}
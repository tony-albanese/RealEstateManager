package com.openclassrooms.realestatemanager.recycler_selection

import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.CUSTOM_TAG

class Lookup(val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    init {
        Log.i(CUSTOM_TAG, "Lookup constructor called")
    }
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {

        Log.i(CUSTOM_TAG, "get Item details called")
        val view = rv.findChildViewUnder(e.x, e.y)
        if (view != null) {
            Log.i(CUSTOM_TAG, "view is not null")
            val holder = rv.getChildViewHolder(view) as ListingAdapter.ListingViewHolder
            return holder.getItemDetails()
        }
        return null
    }
}
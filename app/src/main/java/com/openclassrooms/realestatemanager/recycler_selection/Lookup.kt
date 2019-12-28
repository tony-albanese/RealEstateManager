package com.openclassrooms.realestatemanager.recycler_selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class Lookup(val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {

        val view = rv.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val holder = rv.getChildViewHolder(view) as ListingAdapter.ListingViewHolder
            return holder.getItemDetails()
        }
        return null
    }
}
package com.openclassrooms.realestatemanager.MasterDetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.database_files.Listing

class MasterRecyclerViewAdapter(
        private val parentActivity: ListingListActivity,
        private val twoPane: Boolean
) : RecyclerView.Adapter<MasterRecyclerViewAdapter.ListingViewHolder>() {


    private var listings = emptyList<Listing>()


    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
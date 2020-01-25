package com.openclassrooms.realestatemanager.MasterDetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.Listing
import kotlinx.android.synthetic.main.listing_list_content.view.*

class MasterRecyclerViewAdapter(
        private val parentActivity: ListingListActivity,
        private val twoPane: Boolean
) : RecyclerView.Adapter<MasterRecyclerViewAdapter.ListingViewHolder>() {


    private var listings = emptyList<Listing>()
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { clickedView: View ->
            val listingItem = clickedView.tag as Listing
            if (twoPane) {
                val fragment = ListingDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ListingDetailFragment.ARG_ITEM_ID, listingItem.id)
                    }
                }
                parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.listing_detail_container, fragment)
                        .commit()
            } else {
                val intent = Intent(clickedView.context, ListingDetailActivity::class.java)
                intent.putExtra(ListingDetailFragment.ARG_ITEM_ID, listingItem.id)
                clickedView.context.startActivity(intent)
            }
        }
    }
    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.id_text
        val contentView: TextView = itemView.content

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listing_list_content, parent, false)
        return ListingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listingItem = listings[position]
        holder.idView.text = listingItem.id.toString()
        holder.contentView.text = listingItem.listingPrice

        with(holder.itemView) {
            tag = listingItem
            setOnClickListener(onClickListener)
        }
    }

    internal fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }
}
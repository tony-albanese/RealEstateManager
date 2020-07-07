package com.openclassrooms.realestatemanager.ListingPhotos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R

class ListingPhotoAdapter : RecyclerView.Adapter<ListingPhotoAdapter.PhotoViewHolder>() {


    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listing_photo_item_layout, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

    }


}
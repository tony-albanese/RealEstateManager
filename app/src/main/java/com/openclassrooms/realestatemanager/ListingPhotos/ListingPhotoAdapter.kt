package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R

class ListingPhotoAdapter(val context: Context, var photoList: ArrayList<ListingPhoto>) : RecyclerView.Adapter<ListingPhotoAdapter.PhotoViewHolder>() {


    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.iv_listing_photo)
        val textViewPhotoDescription = itemView.findViewById<TextView>(R.id.tv_listing_photo_description)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listing_photo_item_layout, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val selectedPhoto = photoList[position]
        holder.textViewPhotoDescription.text = selectedPhoto.photoDescription

        Glide.with(context)
                .load(selectedPhoto.photoUri)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder_image))
                .error(context.resources.getDrawable(R.drawable.placeholder_image))
                .into(holder.imageView)
    }
}
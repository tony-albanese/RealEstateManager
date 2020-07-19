package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R

class ListingPhotoAdapter(val context: Context, var photoList: ArrayList<ListingPhoto>) : RecyclerView.Adapter<ListingPhotoAdapter.PhotoViewHolder>() {


    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.iv_listing_photo)
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
        setPic(holder.imageView, selectedPhoto.photoUri)
        //TODO: Implement click listener to display the images.
        //TODO: Add description.
    }

    private fun setPic(imageView: ImageView, photoUri: Uri?) {
        // Get the dimensions of the View
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(photoUri?.path, bmOptions)?.also { bitmap ->
            Glide.with(context)
                    .load(bitmap)
                    .placeholder(context.resources.getDrawable(R.drawable.placeholder_image))
                    .error(context.resources.getDrawable(R.drawable.placeholder_image))
                    .into(imageView)

            //imageView.setImageBitmap(bitmap)
        }
    }
}
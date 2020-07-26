package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R

class DisplayPhotoWindow(
        val context: Context,
        val anchorView: View,
        val photoUri: Uri?
) {

    val layoutInflater: LayoutInflater
    val popupWindow: PopupWindow

    val imageView: ImageView
    val dismissButton: ImageButton

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupContentView: View = layoutInflater.inflate(R.layout.display_photo_layout, null)

        popupContentView.apply {
            imageView = findViewById<ImageView>(R.id.listing_photo)
            dismissButton = findViewById(R.id.ib_window_dismiss)
            dismissButton.bringToFront()
        }

        popupWindow = PopupWindow(popupContentView, CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT, true)

        @Suppress("DEPRECATION")
        Glide.with(context)
                .load(photoUri)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder_image))
                .placeholder(context.resources.getDrawable(R.drawable.placeholder_image))
                .into(imageView)

        dismissButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun show() {
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }
}
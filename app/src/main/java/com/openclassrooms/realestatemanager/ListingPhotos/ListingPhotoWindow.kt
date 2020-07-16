package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.Listing

class ListingPhotoWindow(
        val context: Context,
        val anchorView: View,
        val photoUri: Uri,
        val listing: Listing = Listing()

) {

    val layoutInflater: LayoutInflater
    val popupWindow: PopupWindow

    val imageView: ImageView
    val photoDescriptionEditText: TextInputEditText?
    val okButton: MaterialButton
    val cancelButton: MaterialButton


    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupContentView: View = layoutInflater.inflate(R.layout.photo_selection_layout, null)

        popupContentView.apply {
            imageView = findViewById<ImageView>(R.id.iv_photo_select)
            photoDescriptionEditText = findViewById<TextInputEditText>(R.id.et_listing_photo_description)
            okButton = findViewById(R.id.btn_select_photo)
            cancelButton = findViewById(R.id.btn_cancel_photo)
        }

        popupWindow = PopupWindow(popupContentView, CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT, true)


        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        @Suppress("DEPRECATION")
        Glide.with(context)
                .load(photoUri)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder_image))
                .placeholder(context.resources.getDrawable(R.drawable.placeholder_image))
                .into(imageView)
    }

    fun show() {
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }


}
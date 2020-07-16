package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.Listing

class ListingPhotoWindow(
        val context: Context,
        val anchorView: View,
        val listing: Listing = Listing()

) {

    val layoutInflater: LayoutInflater
    val popupWindow: PopupWindow

    val imageView: ImageView?
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

        popupWindow = PopupWindow(popupContentView, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)


    }

    fun show() {
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
        popupWindow.update(0, 0, popupWindow.getWidth(), popupWindow.getHeight())
        popupWindow.showAsDropDown(anchorView)
    }


}
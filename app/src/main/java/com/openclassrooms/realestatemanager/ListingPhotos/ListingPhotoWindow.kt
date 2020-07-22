package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.AppDatabase
import com.openclassrooms.realestatemanager.database_files.Listing
import com.openclassrooms.realestatemanager.database_files.ListingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListingPhotoWindow(
        val context: Context,
        val anchorView: View,
        val photoUri: Uri,
        val selectedListing: Listing?
) {

    var listener: PhotoSelectionListener? = null

    val layoutInflater: LayoutInflater
    val popupWindow: PopupWindow

    val imageView: ImageView
    val photoDescriptionEditText: TextInputEditText?
    val okButton: MaterialButton
    val cancelButton: MaterialButton
    val deletePhotoButton: ImageButton
    val setHomePhotoButton: ImageButton
    val homeImageTextView: TextView

    val listingRepository: ListingRepository
    val listingPhotoRepository: ListingPhotoRepository

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupContentView: View = layoutInflater.inflate(R.layout.photo_selection_layout, null)

        popupContentView.apply {
            imageView = findViewById<ImageView>(R.id.iv_photo_select)
            photoDescriptionEditText = findViewById<TextInputEditText>(R.id.et_listing_photo_description)
            okButton = findViewById(R.id.btn_select_photo)
            cancelButton = findViewById(R.id.btn_cancel_photo)

            deletePhotoButton = findViewById(R.id.ib_delete_photo)
            setHomePhotoButton = findViewById(R.id.ib_set_main_photo)
            homeImageTextView = findViewById(R.id.tv_showcase_photo_set)

            val listingDao = AppDatabase.getDatabase(context).listingDao()
            val listingPhotoDao = AppDatabase.getDatabase(context).listingPhotoDao()

            listingRepository = ListingRepository(listingDao)
            listingPhotoRepository = ListingPhotoRepository(listingPhotoDao)
        }

        popupWindow = PopupWindow(popupContentView, CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT, true)

        cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        okButton.setOnClickListener {
            createListingPhoto()
            popupWindow.dismiss()
        }

        setHomePhotoButton.setOnClickListener {
            if (homeImageTextView.visibility == View.VISIBLE) {
                homeImageTextView.visibility = View.GONE
            } else {
                homeImageTextView.visibility = View.VISIBLE
            }
        }

        initializeButtonStates()

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

    interface PhotoSelectionListener {
        fun onPhotoSelection(photo: ListingPhoto, isHomeImage: Boolean)
    }

    private fun createListingPhoto() {
        val isHomeImage = homeImageTextView.visibility == View.VISIBLE
        val photo = ListingPhoto(0, selectedListing?.id
                ?: 0, photoDescriptionEditText?.text.toString(), photoUri)
        listener?.onPhotoSelection(photo, isHomeImage)
    }

    private fun initializeButtonStates() {
        CoroutineScope(Dispatchers.IO).launch {
            val listingPhoto = async { listingPhotoRepository.getPhotoByUri(photoUri) }.await()
            initializeUI(selectedListing, listingPhoto)
        }

    }

    private fun initializeUI(listing: Listing?, photo: ListingPhoto) {
        CoroutineScope(Dispatchers.Main).launch {
            //Set the state of the text view to say whether the current photo is the home photo.
            if (photoUri == listing?.listingMainPhotoUri) {
                homeImageTextView.visibility == View.GONE
            } else {
                homeImageTextView.visibility == View.VISIBLE
            }
        }
        //TODO: Set the state of the editing buttons if the current user is the user that owns the listing.
    }
}
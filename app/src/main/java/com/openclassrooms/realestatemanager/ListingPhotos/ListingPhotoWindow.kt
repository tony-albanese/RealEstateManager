package com.openclassrooms.realestatemanager.ListingPhotos

import android.content.Context
import android.net.Uri
import android.util.Log
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
import java.io.File

class ListingPhotoWindow(
        val context: Context,
        val anchorView: View,
        val photoUri: Uri,
        val selectedListing: Listing?,
        val photo: ListingPhoto? = null
) {

    var listener: PhotoSelectionListener? = null

    val layoutInflater: LayoutInflater
    val popupWindow: PopupWindow

    val imageView: ImageView
    val photoDescriptionEditText: TextInputEditText?
    val okButton: MaterialButton
    val deletePhotoButton: MaterialButton
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

            deletePhotoButton = findViewById(R.id.btn_delete_photo)
            setHomePhotoButton = findViewById(R.id.ib_set_main_photo)
            setHomePhotoButton.bringToFront()
            homeImageTextView = findViewById(R.id.tv_showcase_photo_set)

            val listingDao = AppDatabase.getDatabase(context).listingDao()
            val listingPhotoDao = AppDatabase.getDatabase(context).listingPhotoDao()

            listingRepository = ListingRepository(listingDao)
            listingPhotoRepository = ListingPhotoRepository(listingPhotoDao)
        }

        popupWindow = PopupWindow(popupContentView, CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT, true)

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

        deletePhotoButton.setOnClickListener {
            deletePhoto(photoUri)
            popupWindow.dismiss()
        }

        initializeButtonStates()

        photo?.let {
            photoDescriptionEditText?.setText(it.photoDescription)
        }

        if ((selectedListing?.listingMainPhotoUri != photo?.photoUri)) {
            homeImageTextView.visibility = View.GONE
        } else {
            homeImageTextView.visibility = View.VISIBLE
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

    interface PhotoSelectionListener {
        fun onPhotoSelection(photo: ListingPhoto, isHomeImage: Boolean, isNewPhoto: Boolean = true)
        fun onPhotoDelete(uri: Uri, listingId: Long, resultStatus: Boolean)
    }

    private fun createListingPhoto() {
        val isHomeImage = homeImageTextView.visibility == View.VISIBLE
        if (photo == null) {
            val newPhoto = ListingPhoto(0, selectedListing?.id
                    ?: 0, photoDescriptionEditText?.text.toString(), photoUri)
            listener?.onPhotoSelection(newPhoto, isHomeImage)
        } else {
            photo.photoDescription = photoDescriptionEditText?.text.toString()
            listener?.onPhotoSelection(photo, isHomeImage, false)
        }

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

    private fun deletePhoto(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { listingPhotoRepository.deleteListingPhoto(uri) }.await()

            Log.i("DELETE", "Result of deleting the photo from Room: " + result.toString())
            if (result == 1) {
                selectedListing?.apply {
                    Log.i("DELETE", "In selectedListing.apply{}")
                    Log.i("DELETE", "Is the listing main uri the photo to delete: " + (this.listingMainPhotoUri == uri).toString())
                    if (this.listingMainPhotoUri == uri) {
                        listingMainPhotoUri = null
                    }
                    val updateResult = async { listingRepository.updateListing(this@apply) }.await()
                    Log.i("DELETE", "Result of updating the lisitng: " + updateResult.toString())
                    listener?.onPhotoDelete(uri, this.id, updateResult == 1)

                }
            } else {
                val file = File(uri.path ?: "")
                val fileDeleteResult = file.delete()
                Log.i("DELETE", "Result of deleting file: " + fileDeleteResult.toString())
                listener?.onPhotoDelete(uri, selectedListing?.id ?: 0, fileDeleteResult)
            }
        }
    }
}
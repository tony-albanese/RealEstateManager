package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore

class ListingPhotoUtilities(val context: Context) {

    val REQUEST_IMAGE_CAPTURE = 1253

    fun sendTakePictureIntent(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent: Intent ->
            pictureIntent.resolveActivity(context.packageManager)?.also {
                activity.startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }

        }
    }
}


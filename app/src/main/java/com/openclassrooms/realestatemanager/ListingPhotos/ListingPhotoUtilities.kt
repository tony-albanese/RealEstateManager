package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ListingPhotoUtilities(val context: Context) {

    val TAG: String = "PHOTO"
    var currentPhotoPath = ""

    fun createTakePictureIntent(): Pair<Intent, File?> {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
        } catch (e: IOException) {
            null
        }

        photoFile?.also {
            val photoUri: Uri = FileProvider.getUriForFile(context, "com.openclassrooms.realestatemanager.provider", it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        return Pair(intent, photoFile)
    }

    /*
    This method will create an image file that will be used to store the image
    captured from the camera.
     */
    @Throws(IOException::class)
    private fun createImageFile(): File? {

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir: File? = context.filesDir
        Log.i(TAG, "Storage Directory: " + storageDir.toString())
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            Log.i(TAG, "Current Photo Path: " + currentPhotoPath.toString())
            currentPhotoPath = absolutePath
        }


    }

    fun addPhotoToGallery(activity: Activity, uri: Uri) {
        @Suppress("DEPRECATION")
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            mediaScanIntent.data = uri
            activity.sendBroadcast(mediaScanIntent)
        }
    }


}
    //TODO () Declare interfaces: one to handle the photo captured from the camera.
    //TODO () Declare an interface to handle any errors.



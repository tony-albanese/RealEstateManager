package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ListingPhotoUtilities(val context: Context) {

    val TAG: String = "PHOTO"
    val REQUEST_IMAGE_CAPTURE = 1253
    var currentPhotoPath = ""


    fun sendTakePictureIntent(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent: Intent ->
            pictureIntent.resolveActivity(context.packageManager)?.also {

                val photoFile: File? = try {
                    createImageFile()
                } catch (e: IOException) {
                    null
                }

                photoFile?.also {
                    val photoUri: Uri = FileProvider.getUriForFile(context, "com.openclassrooms.realestatemanager.provider", it)
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    Log.i(TAG, photoUri.toString())
                    activity.startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
                }

            }

        }
    }

    /*
    This method will create an image file that will be used to store the image
    captured from the camera.
     */
    @Throws(IOException::class)
    private fun createImageFile(): File? {

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            Log.i(TAG, currentPhotoPath.toString())
            currentPhotoPath = absolutePath
        }

    }

    //TODO () Declare interfaces: one to handle the photo captured from the camera.
    //TODO () Declare an interface to handle any errors.
}


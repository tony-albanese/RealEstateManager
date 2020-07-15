package com.openclassrooms.realestatemanager.ListingPhotos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.openclassrooms.realestatemanager.Utilities.REQUEST_EXTERNAL_WRITE_PERMISSION
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ListingPhotoUtilities(val context: Context, val activity: Activity) : ActivityCompat.OnRequestPermissionsResultCallback {

    val TAG: String = "PHOTO"
    var currentPhotoPath = ""


    lateinit var storageDir: File

    init {
        setStorageDirectory()
    }

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


    private fun getWritePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun setStorageDirectory() {
        if (getWritePermission()) {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        } else {
            storageDir = context.filesDir
            requestWritePermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_WRITE_PERMISSION -> {
                when {
                    grantResults.isEmpty() -> storageDir = context.filesDir

                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

                    else -> storageDir = context.filesDir
                }
            }
        }

    }

    private fun requestWritePermission() {

        ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_WRITE_PERMISSION
        )
    }

}
    //TODO () Declare interfaces: one to handle the photo captured from the camera.
    //TODO () Declare an interface to handle any errors.



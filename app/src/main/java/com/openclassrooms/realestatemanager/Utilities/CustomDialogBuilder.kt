package com.openclassrooms.realestatemanager.Utilities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

class CustomDialogBuilder(
        val context: Context,
        val launchClass: Class<out Activity>
) {

    val builder: AlertDialog.Builder
    internal var myCallback: OnSaveButtonClickCallback

    init {
        builder = AlertDialog.Builder(context)
        myCallback = context as OnSaveButtonClickCallback
    }

    interface OnSaveButtonClickCallback {
        fun onSaveButtonClick()
    }

    fun buildWarningDialog(): AlertDialog.Builder {
        
        return builder.setTitle("Warning")
                .setMessage("Are you sure? Unsaved changes will be discarded")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", createPositiveButtonListener())
    }

    /*
    Method will create the listener that will run when the user selects ok after hitting the back
    button or cancel.
     */

    fun createPositiveButtonListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialogInterface, i ->
            val intent = Intent(context, launchClass)
            context.startActivity(intent)
        }
    }

    fun createSaveListingDialog(): AlertDialog.Builder {
        return builder.setTitle("Save Listing")
                .setMessage("Save the current listing?")
                .setNegativeButton("No", null)
                .setPositiveButton("Save", DialogInterface.OnClickListener { dialogInterface, i ->
                    myCallback
                })
    }
}
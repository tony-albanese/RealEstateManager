package com.openclassrooms.realestatemanager.Utilities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class CustomDialogBuilder(
        val context: Context
) {

    val builder: AlertDialog.Builder

    init {
        builder = AlertDialog.Builder(context)
    }

    fun buildWarningDialog(onYesButtonClickListener: DialogInterface.OnClickListener): AlertDialog.Builder {
        return builder.setTitle("Warning")
                .setMessage("Are you sure? Unsaved changes will be discarded")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", onYesButtonClickListener)
    }
    
    fun createSaveListingDialog(onYesButtonClickListener: DialogInterface.OnClickListener): AlertDialog.Builder {
        return builder.setTitle("Save Listing")
                .setMessage("Save the current listing?")
                .setNegativeButton("No", null)
                .setPositiveButton("Save", onYesButtonClickListener)
    }
}
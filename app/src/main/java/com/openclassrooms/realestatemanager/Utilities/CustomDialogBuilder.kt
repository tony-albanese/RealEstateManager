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

    fun buildDeleteListingWarningDialog(onYesButtonClickListener: DialogInterface.OnClickListener): AlertDialog.Builder? {
        return builder.setTitle("Warning")
                .setMessage("Delete listing? This action cannot be undone")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", onYesButtonClickListener)
        
    }
}
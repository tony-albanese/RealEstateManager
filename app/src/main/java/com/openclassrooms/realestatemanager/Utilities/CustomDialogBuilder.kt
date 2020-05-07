package com.openclassrooms.realestatemanager.Utilities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.Activities.ListingsMapActivity
import com.openclassrooms.realestatemanager.Activities.MainActivity

class CustomDialogBuilder(
        val context: Context
) {

    val builder: AlertDialog.Builder

    init {
        builder = AlertDialog.Builder(context)
    }

    fun buildWarningDialog(onYesButtonClickListener: DialogInterface.OnClickListener, onNoButtonClickListener: DialogInterface.OnClickListener): AlertDialog.Builder {
        return builder.setTitle("Warning")
                .setMessage("Are you sure? Unsaved changes will be discarded")
                .setNegativeButton("Cancel", onNoButtonClickListener)
                .setPositiveButton("Yes", onYesButtonClickListener)
    }

    fun buildDeleteListingWarningDialog(onYesButtonClickListener: DialogInterface.OnClickListener): AlertDialog.Builder? {
        return builder.setTitle("Warning")
                .setMessage("Delete listing? This action cannot be undone")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", onYesButtonClickListener)
        
    }

    fun buildSuccessDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
                .setTitle("Save Listing")
                .setMessage("Listing Saved!")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    dialogInterface.dismiss()
                })

    }

    fun buildRequestListingLocationDialog(listingId: Long): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
                .setTitle("Listing Location")
                .setMessage("Get listing location details?")
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    dialogInterface.dismiss()
                })
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(context, ListingsMapActivity::class.java)
                    intent.putExtra(LISTING_ID, listingId)
                    intent.putExtra(ACTIVITY_TASK, TASK_SELECT_LISTING_LOCATION)
                    context.startActivity(intent)
                })
    }
}
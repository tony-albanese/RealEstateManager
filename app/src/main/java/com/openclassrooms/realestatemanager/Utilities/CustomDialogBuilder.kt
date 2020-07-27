package com.openclassrooms.realestatemanager.Utilities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.Activities.ListingBaseActivity
import com.openclassrooms.realestatemanager.Activities.ListingMapActivities.GeocodeListingLocationActivity

class CustomDialogBuilder(
        val context: Context
) {

    val builder: MaterialAlertDialogBuilder

    init {
        builder = MaterialAlertDialogBuilder(context)
    }

    fun buildWarningDialog(onYesButtonClickListener: DialogInterface.OnClickListener, onNoButtonClickListener: DialogInterface.OnClickListener): MaterialAlertDialogBuilder {
        return builder.setTitle("Warning")
                .setMessage("Are you sure? Unsaved changes will be discarded")
                .setNegativeButton("Cancel", onNoButtonClickListener)
                .setPositiveButton("Yes", onYesButtonClickListener)
    }

    fun buildDeleteListingWarningDialog(onYesButtonClickListener: DialogInterface.OnClickListener): MaterialAlertDialogBuilder? {
        return builder.setTitle("Warning")
                .setMessage("Delete listing? This action cannot be undone")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", onYesButtonClickListener)
        
    }

    fun buildSuccessDialogBuilder(): MaterialAlertDialogBuilder {
        return builder
                .setTitle("Save Listing")
                .setMessage("Listing Saved!")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(context, ListingBaseActivity::class.java)
                    context.startActivity(intent)
                    dialogInterface.dismiss()
                })

    }

    fun buildErrorDialog(): MaterialAlertDialogBuilder {
        return builder
                .setTitle("Save Listing")
                .setMessage("Something went wrong.")
                .setPositiveButton("OK", this.createPositiveErrorButtonLisenter())
    }

    //This listener just dismisses the dialog box then the user clicks OK.
    fun createPositiveErrorButtonLisenter(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
    }

    fun buildRequestListingLocationDialog(listingId: Long): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
                .setTitle("Listing Location")
                .setMessage("Get listing location details?")
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(context, ListingBaseActivity::class.java)
                    context.startActivity(intent)
                    dialogInterface.dismiss()
                })
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    //val intent = Intent(context, ListingsMapActivity::class.java)
                    val intent = Intent(context, GeocodeListingLocationActivity::class.java)
                    intent.putExtra(LISTING_ID, listingId)
                    intent.putExtra(ACTIVITY_TASK, TASK_SELECT_LISTING_LOCATION)
                    context.startActivity(intent)
                })
    }
}
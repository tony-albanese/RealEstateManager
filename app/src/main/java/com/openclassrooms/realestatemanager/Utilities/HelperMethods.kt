package com.openclassrooms.realestatemanager.Utilities

import android.content.Context
import android.content.Intent
import android.view.Menu
import com.openclassrooms.realestatemanager.Activities.EditListingActivity
import com.openclassrooms.realestatemanager.Constants.LISTING_ID_KEY
import com.openclassrooms.realestatemanager.database_files.Listing

class HelperMethods() {

    fun onUnpublishedListingClick(context: Context, unpublishedListings: List<Listing>, menuId: Long) {
        val listingId = unpublishedListings.filter {
            it.id == menuId
        }
        if (listingId.size == 1) {
            val intent = Intent(context, EditListingActivity::class.java)
            intent.putExtra(LISTING_ID_KEY, menuId)
            context.startActivity(intent)
        }
    }

    fun generateUnpublishedListingMenu(menu: Menu?, menuItemPosition: Int, unpublishedListings: List<Listing>) {
        val subMenu = menu?.getItem(menuItemPosition)?.subMenu.apply {
            for (listing in unpublishedListings) {
                this?.add(Menu.NONE, listing.id.toInt(), Menu.NONE, listing.listingStreetAddress)
            }
        }
    }

    fun onAddNewListingClick(context: Context) {
        val intent = Intent(context, EditListingActivity::class.java)
        intent.putExtra(LISTING_ID_KEY, 0.toLong())
        context.startActivity(intent)
    }

    fun onEditListingClick(context: Context, id: Long) {
        val intent = Intent(context, EditListingActivity::class.java)
        intent.putExtra(LISTING_ID_KEY, id)
        context.startActivity(intent)
    }
}
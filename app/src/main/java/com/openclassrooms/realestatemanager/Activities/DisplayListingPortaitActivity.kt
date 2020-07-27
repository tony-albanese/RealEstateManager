package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ListingInformationDetailLayoutBinding
import kotlinx.android.synthetic.main.listing_information_detail_layout.*

class DisplayListingPortaitActivity : ListingBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val selectedListingId = intent.getLongExtra("LISTING_ID", 0)

        if (selectedListingId != 0.toLong()) {
            globalVariables.selectedPortraitListingId = selectedListingId
            listingViewModel.getListingForPortraitMode(selectedListingId)
            listingPhotoViewModel.getPhotosForLisiting(selectedListingId)
        } else {
            listingViewModel.getListingForPortraitMode(globalVariables.selectedPortraitListingId)
            listingPhotoViewModel.getPhotosForLisiting(globalVariables.selectedListingId)
        }
    }

    override fun setLayoutBinding() {
        val binding: ListingInformationDetailLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listing_information_detail_layout)
        binding.viewModel = listingViewModel
        binding.lifecycleOwner = this

        portrait_toolbar?.setTitle("Your Listing")
        setSupportActionBar(portrait_toolbar)
        portrait_app_bar?.visibility = View.VISIBLE
    }

    override fun getAnchorView(): View {
        return findViewById(R.id.display_listing_constraint_layout)
    }
}

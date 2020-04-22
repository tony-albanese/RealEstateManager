package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database_files.ListingViewModel
import com.openclassrooms.realestatemanager.databinding.ListingInformationDetailLayoutBinding
import kotlinx.android.synthetic.main.listing_information_detail_layout.*

class DisplayListingPortaitActivity : AppCompatActivity() {

    lateinit var listingViewModel: ListingViewModel
    /*
    This is the activity that will be launched if the device is in portrait mode.
    It will display the info for a selected listing.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listingViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(application)).get(ListingViewModel::class.java)

        val binding: ListingInformationDetailLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listing_information_detail_layout)
        binding.viewModel = listingViewModel
        binding.lifecycleOwner = this


        portrait_toolbar.setTitle("Your Listing")
        setSupportActionBar(portrait_toolbar)
        portrait_app_bar.visibility = View.VISIBLE

        val intent = intent
        intent.getLongExtra("LISTING_ID", 0).let {
            if (it != 0.toLong()) {
                listingViewModel.getListingForPortraitMode(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.setEnabled(false)
        menu?.getItem(0)?.setVisible(false)
        menu?.getItem(2)?.setEnabled(false)
        menu?.getItem(2)?.setVisible(false)
        return true
    }
}

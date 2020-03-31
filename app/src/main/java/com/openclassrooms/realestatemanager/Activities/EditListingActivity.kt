package com.openclassrooms.realestatemanager.Activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModel
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModelFactory
import kotlinx.android.synthetic.main.listing_edit_layout.*
import kotlinx.android.synthetic.main.listing_form_input_layout.*

class EditListingActivity : AppCompatActivity() {

    lateinit var viewModel: ListingEditViewModel
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listing_edit_layout)

        edit_listing_toolbar.title = "Edit Listing"
        setSupportActionBar(edit_listing_toolbar)

        spinner = findViewById<Spinner>(R.id.spinner_listing_type)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.listing_type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val viewModelFactory = ListingEditViewModelFactory(application, this, null)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ListingEditViewModel::class.java)
        /*
        Load the UI
         */
        loadUI()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_listing_menu, menu)
        return true
    }

    override fun onPause() {
        super.onPause()
        saveUI()
    }


    fun loadUI() {
        et_listing_street_address.setText(viewModel.streetAddress)
        et_listing_city.setText(viewModel.city)
        et_listing_zipcode.setText(viewModel.zipCode)
        et_listing_sales_price.setText(viewModel.salesPrice)

        spinner.setSelection(viewModel.getSpinnerPosition())
        seekbar_total_rooms.progress = viewModel.getNumberOfRooms()
        seekbar_bedrooms.progress = viewModel.getBedrooms()

    }

    fun saveUI() {
        viewModel.saveSpinnerPosition(spinner_listing_type.selectedItemPosition)
        viewModel.saveNumberOfRooms(seekbar_total_rooms.progress)
        viewModel.saveNumberOfBedroom(seekbar_bedrooms.progress)
    }

}

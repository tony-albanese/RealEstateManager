package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModel
import kotlinx.android.synthetic.main.listing_edit_layout.*
import kotlinx.android.synthetic.main.listing_form_input_layout.*

class EditListingActivity : AppCompatActivity() {

    lateinit var viewModel: ListingEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listing_edit_layout)

        edit_listing_toolbar.title = "Edit Listing"
        setSupportActionBar(edit_listing_toolbar)

        val spinner = findViewById<Spinner>(R.id.spinner_listing_type)

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

        viewModel = ViewModelProvider(this, SavedStateViewModelFactory(application, this))
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

    fun loadUI() {
        tv_listing_street_address.setText(viewModel.address)
    }

}

package com.openclassrooms.realestatemanager.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.listingmanagement.ListingDatePicker
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModel
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModelFactory
import kotlinx.android.synthetic.main.listing_edit_layout.*
import kotlinx.android.synthetic.main.listing_form_input_layout.*
import java.util.*

class EditListingActivity : AppCompatActivity() {

    lateinit var viewModel: ListingEditViewModel
    lateinit var spinner: Spinner
    lateinit var sellingDateTextView: TextView
    lateinit var listingDateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listing_edit_layout)

        edit_listing_toolbar.title = "Edit Listing"
        setSupportActionBar(edit_listing_toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edit_listing_toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        spinner = findViewById<Spinner>(R.id.spinner_listing_type)

        listingDateTextView = findViewById(R.id.tv_listing_date)
        sellingDateTextView = findViewById(R.id.tv_selling_date)
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

        tv_listing_date.setOnClickListener {
            val datePicker = ListingDatePicker(this, Calendar.getInstance(), listingDateTextView, setDateCallback)
            datePicker.show(supportFragmentManager, "listingDatePicker")
        }

        tv_selling_date.setOnClickListener {
            val datePicker = ListingDatePicker(this, Calendar.getInstance(), sellingDateTextView, setDateCallback)
            datePicker.show(supportFragmentManager, "sellingDatePicker")
        }

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
        seekbar_bathrooms.progress = (viewModel.getBathrooms() * 2).toInt()
    }

    fun saveUI() {
        viewModel.saveSpinnerPosition(spinner_listing_type.selectedItemPosition)
        viewModel.saveNumberOfRooms(seekbar_total_rooms.progress)
        viewModel.saveNumberOfBedroom(seekbar_bedrooms.progress)
        viewModel.saveNumberOfBathrooms((seekbar_bathrooms.progress / 2).toDouble())
    }

    /*
    This is the function that will be called when the date has been set.
    It will be used to set the selling date.
     */
    val setDateCallback: (TextView, String) -> Unit = { textView: TextView, s: String ->
        textView.text = s
    }
}

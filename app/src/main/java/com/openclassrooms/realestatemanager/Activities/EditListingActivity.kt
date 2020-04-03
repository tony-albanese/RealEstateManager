package com.openclassrooms.realestatemanager.Activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.CustomDialogBuilder
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
            initiateExitActivitySequence()
        }

        btn_cancel_listing.setOnClickListener {
            initiateExitActivitySequence()
        }

        btn_save_listing.setOnClickListener {

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
    

        tv_listing_date.setOnClickListener {
            val datePicker = ListingDatePicker(this, Calendar.getInstance(), listingDateTextView, setDateCallback)
            datePicker.show(supportFragmentManager, "listingDatePicker")
        }

        tv_selling_date.setOnClickListener {
            val datePicker = ListingDatePicker(this, Calendar.getInstance(), sellingDateTextView, setDateCallback)
            datePicker.show(supportFragmentManager, "sellingDatePicker")
        }
    }


    /*
    This is the function that will be called when the date has been set.
    It will be used to set the selling date.
     */
    val setDateCallback: (TextView, String) -> Unit = { textView: TextView, s: String ->
        textView.text = s
    }

    /*
    This method will initiate the sequence to exit the Activity when the user clicks, save, cancel,
    delete, or save.
    TODO: Implement this method.
     */
    fun initiateExitActivitySequence() {
        val dialogBuilder = CustomDialogBuilder(this)
        dialogBuilder.buildWarningDialog(confirmDiscardChangesClickListener())
                .show()
    }

    /*
    This code will be executed if the user agrees to leave the activity and discard changes
    to the form.
     */
    fun confirmDiscardChangesClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialogInterface, i ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun confirmListingDeleteClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialogInterface, i ->
            Toast.makeText(this, "Listing deleted", Toast.LENGTH_SHORT).show()
        }
    }

    //Here's where the menu code will go.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_listing_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.menu_item_delete_listing -> {
                val dialogBuilder = CustomDialogBuilder(this)
                dialogBuilder.buildDeleteListingWarningDialog(confirmListingDeleteClickListener())
                        ?.show()
                return true
            }
            else -> return true
        }
    }
}
package com.openclassrooms.realestatemanager.Activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.Constants.LISTING_ID_KEY
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utilities.AddressTextWatcher
import com.openclassrooms.realestatemanager.Utilities.ConversionUtilities
import com.openclassrooms.realestatemanager.Utilities.CustomDialogBuilder
import com.openclassrooms.realestatemanager.Utilities.FormValidatorUtilities
import com.openclassrooms.realestatemanager.databinding.ListingEditLayoutBinding
import com.openclassrooms.realestatemanager.listingmanagement.ListingDatePicker
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModel
import com.openclassrooms.realestatemanager.listingmanagement.ListingEditViewModelFactory
import kotlinx.android.synthetic.main.listing_edit_layout.*
import kotlinx.android.synthetic.main.listing_form_input_layout.*
import java.util.*
import kotlin.collections.HashMap

class EditListingActivity : AppCompatActivity(), OnItemSelectedListener {

    lateinit var viewModel: ListingEditViewModel
    lateinit var spinner: Spinner
    lateinit var saveButton: Button
    lateinit var sellingDateTextView: TextView
    lateinit var listingDateTextView: TextView
    lateinit var salePriceEditText: EditText
    lateinit var listingAreaEditText: EditText
    lateinit var addressEditText: EditText
    lateinit var calendar: Calendar

    var saveListingToFile: Boolean = true
    val viewHashMap = HashMap<Int, View>()

    lateinit var locale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calendar = Calendar.getInstance()

        val intent: Intent? = intent
        val passedListingId: Long? = intent?.getLongExtra(LISTING_ID_KEY, 0) ?: 0

        //Setup DataBinding.
        locale = Locale("EN", "US")
        val binding: ListingEditLayoutBinding = DataBindingUtil.setContentView(this, R.layout.listing_edit_layout)
        val viewModelFactory = ListingEditViewModelFactory(application, this, calendar, passedListingId!!, null)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ListingEditViewModel::class.java)

        binding.listingEditViewModel = viewModel
        binding.locale = locale
        binding.lifecycleOwner = this

        //Initialize variables.
        spinner = findViewById<Spinner>(R.id.spinner_listing_type)
        salePriceEditText = findViewById(R.id.et_listing_sales_price)
        listingAreaEditText = findViewById(R.id.et_listing_area)
        listingDateTextView = findViewById(R.id.tv_listing_date)
        sellingDateTextView = findViewById(R.id.tv_selling_date)
        addressEditText = findViewById(R.id.et_listing_street_address)
        saveButton = findViewById(R.id.btn_save_listing)

        initializeViewHashMap()

        //Setup the action bar.
        edit_listing_toolbar.title = "Edit Listing"
        setSupportActionBar(edit_listing_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
            spinner.setSelection(adapter.getPosition(viewModel.currentListing.value?.toString()))
        }

        //Set up the listeners.
        spinner.onItemSelectedListener = this
        edit_listing_toolbar.setNavigationOnClickListener {
            initiateExitActivitySequence()
        }

        btn_cancel_listing.setOnClickListener {
            initiateExitActivitySequence()
        }

        saveButton.setOnClickListener {
            val data = viewModel.currentListing.value.toString()
            val successDialogBuilder = CustomDialogBuilder(this)
            viewModel.saveListingToDatabase(successDialogBuilder.buildSuccessDialogBuilder())
            System.out.println(data)
        }


        tv_listing_date.setOnClickListener {
            val datePicker = ListingDatePicker(this, Calendar.getInstance(), listingDateTextView, setDateCallback)
            datePicker.show(supportFragmentManager, "listingDatePicker")
        }

        tv_selling_date.setOnClickListener {
            val datePicker = ListingDatePicker(this, Calendar.getInstance(), sellingDateTextView, setDateCallback)
            datePicker.show(supportFragmentManager, "sellingDatePicker")
        }

        salePriceEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            val editText = view as EditText
            if (hasFocus) {

            } else {
                val currentText = editText.text.toString()
                if (!currentText.isNullOrEmpty()) {
                    editText.setText(ConversionUtilities.reformatCurrencyString(currentText, locale))
                    viewModel.updateListingPrice(ConversionUtilities.formatCurrencyToInteger(currentText))
                } else {
                    editText.setText(ConversionUtilities.reformatCurrencyString("0", locale))
                    viewModel.updateListingPrice(0)
                }
            }
        }
        listingAreaEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            val areaTextView = view as TextView
            if (hasFocus) {

            } else {
                if (areaTextView.text.isNullOrBlank() || areaTextView.text.isEmpty()) {
                    areaTextView.setText("0")
                }
            }
        }

        addressEditText.addTextChangedListener(AddressTextWatcher(saveButton))

        switch_is_published.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.currentListing.value?.listingIsPublished = isChecked
            if (isChecked) {
                FormValidatorUtilities.validateForm(viewHashMap)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.saveListingToFile && saveListingToFile) {
            viewModel.saveListingToFile()
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
        saveListingToFile = false
        val dialogBuilder = CustomDialogBuilder(this)
        dialogBuilder.buildWarningDialog(confirmDiscardChangesClickListener(), cancelDiscardChangesClickListener())
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

    fun cancelDiscardChangesClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialogInterface, i ->
            saveListingToFile = true
            dialogInterface.dismiss()
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

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        viewModel.currentListing.value?.listingType = parent?.getItemAtPosition(position).toString()
    }

    fun initializeViewHashMap() {
        viewHashMap.put(et_listing_area.id, et_listing_area)
        viewHashMap.put(et_listing_city.id, et_listing_city)
        viewHashMap.put(et_listing_zipcode.id, et_listing_zipcode)
        viewHashMap.put(et_listing_area.id, et_listing_area)
        viewHashMap.put(et_listing_sales_price.id, et_listing_sales_price)
        viewHashMap.put(addressEditText.id, addressEditText)
    }
}
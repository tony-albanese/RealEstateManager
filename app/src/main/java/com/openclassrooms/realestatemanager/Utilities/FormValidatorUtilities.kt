package com.openclassrooms.realestatemanager.Utilities

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import java.util.regex.Pattern

object FormValidatorUtilities {

    fun validateForm(viewMap: HashMap<Int, View>) {

        viewMap.forEach { (i, view) ->
            when (view) {
                is TextInputEditText -> {
                    val materialEditText = view as TextInputEditText
                    val text = materialEditText?.text?.toString()?.trim() ?: ""
                    if (text.isBlank()) {
                        materialEditText.setError("This is blank.")
                    }
                }
                is EditText -> {
                    val editText = view as EditText
                    val text = editText?.text?.toString()?.trim() ?: ""
                    if (text.isBlank()) {
                        editText.setBackgroundColor(Color.RED)
                    }
                }
                is TextView -> {
                    val textView = view as TextView
                    val text = textView?.text?.toString()?.trim() ?: ""
                    if (text.isBlank()) {
                        textView.setBackgroundColor(Color.RED)
                    }

                }

                else -> {

                }
            }
        }

        viewMap.forEach { (i, view) ->
            when (i) {
                R.id.et_listing_area -> {
                    val editText = view as EditText
                    val textIsNumeric = this.expressionContainsOnlyNumerals(editText.text.toString())

                    if (!textIsNumeric) {
                        editText.setBackgroundColor(Color.RED)
                    }
                }

                R.id.et_listing_zipcode -> {
                    val editText = view as TextInputEditText
                    val zipCode = editText.text.toString()
                    val correctLength = (zipCode.length >= 5 && zipCode.length <= 10)
                    if (!correctLength) {
                        editText.setError("Zip Code is wrong")
                    }
                }

                R.id.et_listing_sales_price -> {
                    val editText = view as TextInputEditText
                    val price = editText.text.toString()
                    if (!price.isNullOrBlank() && !price.isNullOrEmpty()) {
                        val priceAsInteger = price.filter {
                            it.isDigit()
                        }.toInt()
                        if (priceAsInteger.equals(0)) {
                            editText.setError("Listing price is 0.")
                        }
                    }

                }
            }
        }

    }


    fun expressionContainsOnlyNumerals(testString: String): Boolean {

        val regExString = "^[0-9]*\$"
        val pattern = Pattern.compile(regExString)
        return pattern.matcher(testString).matches()
    }
}

class AddressTextWatcher(val view: View) : TextWatcher {

    override fun afterTextChanged(editTextValue: Editable?) {
        view.isEnabled = !(editTextValue.toString().isNullOrBlank() || editTextValue?.isEmpty() ?: false)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


}
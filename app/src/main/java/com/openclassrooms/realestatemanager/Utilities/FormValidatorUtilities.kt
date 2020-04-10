package com.openclassrooms.realestatemanager.Utilities

import android.text.Editable
import android.text.TextWatcher
import android.view.View

object FormValidatorUtilities {


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
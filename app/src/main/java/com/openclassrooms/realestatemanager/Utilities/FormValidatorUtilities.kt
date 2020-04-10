package com.openclassrooms.realestatemanager.Utilities

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.get
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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
                is TextInputLayout -> {
                    val layout = view as TextInputLayout
                    val materialEditText = layout.get(0) as TextInputEditText
                    val text = materialEditText?.text?.toString()?.trim() ?: ""
                    if (text.isBlank()) {
                        materialEditText.setError("This is blank.")
                    }

                }
                else -> {

                }
            }
        }


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
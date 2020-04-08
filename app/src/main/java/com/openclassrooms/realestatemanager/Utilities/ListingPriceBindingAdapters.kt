package com.openclassrooms.realestatemanager.Utilities

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod

object ListingPriceBindingAdapters {

    val TAG: String = "PriceConversion"

    @BindingAdapter("listingPrice")
    @JvmStatic
    fun setListingPrice(et: EditText, value: String) {
        Log.i(TAG, "Binding adapter int to String called")
        if (et.text.toString() != value) {
            et.setText(value)
        }

    }

    @InverseBindingAdapter(attribute = "listingPrice")
    @JvmStatic
    fun getListingPrice(et: EditText): String {
        Log.i(TAG, "Binding adpater string to int called")
        val etText = et.text.toString()
        return etText
    }


    @BindingAdapter("listingPriceAttrChanged")
    @JvmStatic
    fun setListener(
            et: EditText,
            attrChange: InverseBindingListener
    ) {
        Log.i(TAG, "setListener called")
        et.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {

            } else {
                if (et.text.toString().isNullOrBlank() || et.text.toString().isBlank()) {
                    et.setText("$0")
                }
                val currentText = et.text.toString()
                Log.i(TAG, "Current text: " + currentText)
                //TODO This is where the string needs to be formatted and set.
                et.setText(currentText + "$$$")
                attrChange.onChange()
            }

        }
    }
}

object ListingPriceConverters {

    val TAG: String = "PriceConversion"
    @InverseMethod("priceStringToInteger")
    @JvmStatic
    fun intToPriceString(value: Int): String {
        Log.i(TAG, "intToPriceString called")
        return "$ " + value.toString()
    }

    @JvmStatic
    fun priceStringToInteger(value: String): Int {
        Log.i(TAG, "priceStringToInteger called")
        if (value.isNullOrEmpty()) {
            return 0
        }

        return try {
            val integerValue = value.filter {
                it.isDigit()
            }
            return integerValue.toInt()
        } catch (e: NumberFormatException) {
            return 0
        }
    }
}
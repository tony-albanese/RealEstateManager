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

    @BindingAdapter("listing_price")
    @JvmStatic
    fun setListingPrice(et: EditText, value: String) {
        Log.i(TAG, "Binding adapter int to String called")
        et.text.clear()
        et.setText(value)
    }

    @InverseBindingAdapter(attribute = "listing_price")
    @JvmStatic
    fun getListingPrice(et: EditText): String {
        Log.i(TAG, "Binding adpater string to int called")
        val etText = et.text.toString()
        return etText
    }


    @BindingAdapter("listing_priceAttrChanged")
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
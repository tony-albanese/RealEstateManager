package com.openclassrooms.realestatemanager.Utilities

import android.util.Log
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod
import java.util.*

object ListingPriceBindingAdapters {

    val TAG: String = "DEBUG"

    @BindingAdapter("listingPrice")
    @JvmStatic
    fun setListingPrice(et: EditText, value: String) {
        Log.i(TAG, "Binding adapter setListingPrice() called")
        if (et.text.toString() != value) {
            et.setText(value)
        }

    }

    @InverseBindingAdapter(attribute = "listingPrice")
    @JvmStatic
    fun getListingPrice(et: EditText): String {
        Log.i(TAG, "Binding adpater getListingPrice() called")
        val etText = et.text.toString()
        return etText
    }


    @BindingAdapter("listingPriceAttrChanged")
    @JvmStatic
    fun setListener(
            et: EditText,
            attrChange: InverseBindingListener
    ) {
        Log.i("DEBUG", "Binding Adapter setListener() called")
        attrChange.onChange()
    }
}

object ListingPriceConverters {

    val TAG: String = "DEBUG"
    @InverseMethod("priceStringToInteger")
    @JvmStatic
    fun intToPriceString(locale: Locale, value: Int): String {
        Log.i(TAG, "Method intToPriceString() called")
        return ConversionUtilities.formatCurrencyIntToString(value, locale)
    }

    @JvmStatic
    fun priceStringToInteger(locale: Locale, value: String): Int {
        Log.i(TAG, "Inverse method priceStringToInteger() called")
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
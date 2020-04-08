package com.openclassrooms.realestatemanager.Utilities

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object ListingPriceBindingAdapters {

    val TAG: String = "BindingAdapter"

    @BindingAdapter("listing_price")
    fun setListingPrice(et: EditText, value: String) {
        Log.i(TAG, "Binding adapter int to String called")
        et.text.clear()
        et.setText(value)
    }

    @InverseBindingAdapter(attribute = "listing_price")
    fun getListingPrice(et: EditText): String {
        Log.i(TAG, "Binding adpater string to int called")
        val etText = et.text.toString()
        return etText
    }

    @BindingAdapter("listing_priceAttrChanged")
    fun setListeners(
            et: EditText,
            attrChange: InverseBindingListener
    ) {
        Log.i(TAG, "setListener called")

        et.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {

            } else {
                if (et.text.toString().isNullOrBlank() || et.text.toString().isBlank()) {
                    et.setText("0")
                    attrChange.onChange()
                }
                attrChange.onChange()
            }

        }
    }
}

package com.openclassrooms.realestatemanager.listingmanagement

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class ListingDatePicker(val c: Context, val calendar: Calendar, val tv: TextView, val callback: (TextView, String) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(c,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
    }

    /*
    TODO: Format the date properly and update the textview.
     */
    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        callback(tv, "Picker callback called.")
    }
}
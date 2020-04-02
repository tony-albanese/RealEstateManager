package com.openclassrooms.realestatemanager.Utilities

import java.text.SimpleDateFormat
import java.util.*

class DateUtilities {

    companion object {
        val USA_DATE_FORMAT: String = "MM/dd/yyyy"
        val EUROPEAN_DATE_FORMAT: String = "dd/MM/yyyy"
        fun getDateString(calendar: Calendar, format: String = USA_DATE_FORMAT): String {
            val dateFormat = SimpleDateFormat(format)
            return dateFormat.format(calendar.time)
        }
    }

}
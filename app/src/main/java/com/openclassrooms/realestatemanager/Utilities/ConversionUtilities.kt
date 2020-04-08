package com.openclassrooms.realestatemanager.Utilities

import java.text.NumberFormat
import java.util.*

object ConversionUtilities {

    fun formatCurrencyIntToString(amount: Int, locale: Locale): String {
        val value = locale.let { locale: Locale ->
            NumberFormat.getCurrencyInstance(locale).apply {
                this.maximumFractionDigits = 0
                currency = Currency.getInstance("USD")
            }
        }
        return value.format(amount.toLong())
    }

    fun formatCurrencyToInteger(amount: String): Int {
        val convertedAmount = amount.filter {
            it.isDigit()
        }
        return convertedAmount.toInt()
    }
}
package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.Utilities.ConversionUtilities
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class UtiltiesTests {
    private lateinit var locale: Locale

    @Before
    fun initializeVariables() {
        locale = Locale("EN", "US")
    }

    @Test
    fun testIntToStringConversion() {
        val expectedAmount: String = "$1,000"

        val convertedAmount = ConversionUtilities.formatCurrencyIntToString(1000, locale)
        val isEqual = expectedAmount.compareTo(convertedAmount) == 0
        assertEquals(expectedAmount, convertedAmount)
    }

    @Test
    fun testStringToIntCurrencyConversion() {
        val testString = "$2,002"
        val priceAsInt = ConversionUtilities.formatCurrencyToInteger(testString)
        val expectedInteger = 2002
        assertEquals(expectedInteger, priceAsInt)
    }
}
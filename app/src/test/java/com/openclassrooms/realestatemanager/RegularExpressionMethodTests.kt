package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.Utilities.FormValidatorUtilities
import junit.framework.Assert.assertEquals
import org.junit.Test

class RegularExpressionMethodTests {

    val alphaNumericString = "43sk2@"
    val numericString = "88268892"

    @Test
    fun testStringWithNonNumericCharacters() {
        val containsNonNumericCharacters = FormValidatorUtilities.expressionContainsOnlyNumerals(alphaNumericString)

        assertEquals(false, containsNonNumericCharacters)
    }

    @Test
    fun testStringWithOnlyNumerals() {
        val containsOnlyNumerals = FormValidatorUtilities.expressionContainsOnlyNumerals(numericString)
        assertEquals(true, containsOnlyNumerals)
    }
}
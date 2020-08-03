package com.openclassrooms.realestatemanager.Login

import android.util.Patterns

sealed class EmailString(email: String) {
    companion object {
        operator fun invoke(email: String): Email? {
            return if (Validator.isValidEmail(email)) Email(email) else null
        }
    }

    object Validator {
        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    data class Email(val address: String) : EmailString(address) {

    }
}
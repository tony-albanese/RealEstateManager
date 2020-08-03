package com.openclassrooms.realestatemanager.Login

data class RegistrationRequest(
        val firstName: String,
        val lastName: String,
        val password: String,
        val passwordHint: String
) {
}
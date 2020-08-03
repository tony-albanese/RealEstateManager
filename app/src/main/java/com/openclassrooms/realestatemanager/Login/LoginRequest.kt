package com.openclassrooms.realestatemanager.Login

data class LoginRequest(
        val userEmail: String,
        val password: String
) {
}
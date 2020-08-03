package com.openclassrooms.realestatemanager.Login

sealed class LoginResponse<out T : Any?> {

    data class Success<out T : Any>(val data: T) : LoginResponse<T>()
    data class ExceptionError(val exception: Exception) : LoginResponse<Nothing>()
    data class Error(val message: String) : LoginResponse<Nothing>()
}
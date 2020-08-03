package com.openclassrooms.realestatemanager.Login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginRepository(
        val loginDataSource: LoginDataSource
) {

    fun register(request: RegistrationRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = loginDataSource.registerUser(request)
            handleRegisrationResponse(id)
        }

    }

    private fun handleRegisrationResponse(roomResult: Long) {

    }

}
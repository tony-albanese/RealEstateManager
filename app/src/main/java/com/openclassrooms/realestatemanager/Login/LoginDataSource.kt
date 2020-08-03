package com.openclassrooms.realestatemanager.Login

import java.util.*

class LoginDataSource(val loginDao: LoginDao) {


    fun registerUser(registrationRequest: RegistrationRequest?) {
        val uuid = UUID.randomUUID()
        val agent = registrationRequest?.passwordHint?.let { Agent(0, uuid, registrationRequest.firstName, registrationRequest.lastName, registrationRequest.password, it) }
        
    }

}
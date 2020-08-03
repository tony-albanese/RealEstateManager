package com.openclassrooms.realestatemanager.Login

import java.util.*

class LoginDataSource(val loginDao: LoginDao) {

    suspend fun registerUser(registrationRequest: RegistrationRequest?): Long {
        val uuid = UUID.randomUUID()
        val agent = Agent(0, uuid, registrationRequest?.firstName, registrationRequest?.lastName, registrationRequest?.email, registrationRequest?.password, registrationRequest?.passwordHint
                ?: "")
        return loginDao.registerAgent(agent)
    }

    suspend fun registerAgentFirebase(agent: Agent) {

    }


}
package com.openclassrooms.realestatemanager.Login

class LoginRepository(
        val loginDao: LoginDao
) {

    suspend fun registerAgent(agent: Agent) {
        loginDao.registerAgent(agent)
    }

    suspend fun getAgentByEmail(email: String): Agent {
        return loginDao.getAgentByEmail(email)
    }
}
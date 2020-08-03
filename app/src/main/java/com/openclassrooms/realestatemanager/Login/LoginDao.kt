package com.openclassrooms.realestatemanager.Login

import androidx.room.*

@Dao
interface LoginDao {
    @Query("SELECT * FROM table_agents WHERE agentEmail = :email")
    fun getAgentByEmail(email: String): Agent

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun registerAgent(agent: Agent): Long

    @Update
    fun updateAgentInfor(agent: Agent): Long
}
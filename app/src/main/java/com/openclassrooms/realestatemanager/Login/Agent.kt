package com.openclassrooms.realestatemanager.Login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "table_agents")
data class Agent(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = "agentId") var uuid: UUID = UUID.fromString("0"),
        @ColumnInfo(name = "agentFirstName") var agentFirstName: String = "",
        @ColumnInfo(name = "agentLastName") var agentLastName: String = "",
        @ColumnInfo(name = "agentEmail") var agentEmail: String = "",
        @ColumnInfo(name = "password") var password: String = "",
        @ColumnInfo(name = "password_hint") var passwordHint: String = ""
) {
}
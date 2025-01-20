package com.example.cafefinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "locaties")
data class Locatie(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val address: String = ""
)

package com.example.cafefinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locaties")
data class Locatie(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val address: String = ""
)

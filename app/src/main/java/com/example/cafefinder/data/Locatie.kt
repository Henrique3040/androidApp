package com.example.cafefinder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locaties")
data class Locatie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val address: String,
)
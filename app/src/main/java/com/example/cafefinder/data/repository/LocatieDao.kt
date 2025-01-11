package com.example.cafefinder.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.model.LocatieRoom

@Dao
interface LocatieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locatie: Locatie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locaties: List<Locatie>)

    @Query("SELECT * FROM locaties")
    suspend fun getAllLocaties(): List<Locatie>

    @Delete
    suspend fun delete(locatieid: Locatie)

    @Query("DELETE FROM locaties WHERE id = :locatieId")
    suspend fun deleteLocatieById(locatieId: String)



}
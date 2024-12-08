package com.example.cafefinder.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocatieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addLocatie(locatie: Locatie)

    @Query("SELECT * FROM locaties")
    fun getAllLocaties(): LiveData<List<Locatie>>


}
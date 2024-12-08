package com.example.cafefinder.data

import androidx.lifecycle.LiveData

class LocatieRepository (private val locatieDao: LocatieDao)
{
    val getAllLocaties: LiveData<List<Locatie>> = locatieDao.getAllLocaties()

    suspend fun addLocatie(locatie: Locatie) {
        locatieDao.addLocatie(locatie)
    }
}
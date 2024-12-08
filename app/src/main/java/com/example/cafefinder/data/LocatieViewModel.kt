package com.example.cafefinder.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocatieViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Locatie>>
    private val repository: LocatieRepository

    init {
        val locatieDao = LocatieDatabase.getDatabase(application).locatieDao()
        repository = LocatieRepository(locatieDao)
        readAllData = repository.getAllLocaties
    }



    fun addLocatie(locatie: Locatie) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLocatie(locatie)
        }
    }

}
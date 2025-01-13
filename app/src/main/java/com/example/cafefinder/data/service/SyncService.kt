package com.example.cafefinder.data.service

import android.content.Context
import com.example.cafefinder.data.database.AppDatabase
import com.example.cafefinder.data.database.LocatieStore
import com.example.cafefinder.data.model.Locatie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncService (private val context: Context ){

    private val locatieStore = LocatieStore(context)
    private val locatieDao = AppDatabase.getDatabase(context).locatieDao()

    /**
    * functie om locaties te saven in de room database en firebase
    * */
    suspend fun syncSaveLocaties(locatie: Locatie) {
        locatieStore.saveLocatie(locatie).collect{ documentId ->
            if (documentId != null) {
                println("Locatie saved with ID: $documentId")
            }else{
                println("Error saving locatie")
            }
        }
    }

    /**
    * functie om alle locaties uit de room database te halen
    * */
    suspend fun getAllLocatiesFromRoom(): List<Locatie> {
        return locatieStore.getAllLocatiesFromRoom()
    }

    /**
    * functie om alle locaties uit de room database en firebase te verwijderen
    *
    *
    * */
    suspend fun deleteLocatie(locatieId: String) {

        locatieStore.deleteLocatie(locatieId).collect { success ->
            if (success) {
                println("Locatie deleted with ID: $locatieId")
            } else {
                println("Error deleting locatie with ID: $locatieId")
            }
        }
    }



    /**
    *
    * functie om alle locaties uit firebase database en room database te synchroniseren
    *
    * */
    fun syncLocatiesFromFirebaseToRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            locatieStore.getAllLocaties().collect { locaties ->
                locaties.forEach { locatie ->
                    val existingLocatie = locatieDao.getLocatieById(locatie.id)
                    if (existingLocatie == null) {
                        locatieDao.insert(locatie)
                    }
                }
            }
        }

    }


}
package com.example.cafefinder.data.service

import android.content.Context
import com.example.cafefinder.data.database.LocatieStore
import com.example.cafefinder.data.model.Locatie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncService (private val context: Context ){

    private val locatieStore = LocatieStore(context)


    suspend fun syncLocatie(locatie: Locatie) {
        locatieStore.saveLocatie(locatie).collect{ documentId ->
            if (documentId != null) {
                println("Locatie saved with ID: $documentId")
            }else{
                println("Error saving locatie")
            }
        }
    }

    suspend fun getAllLocatiesFromRoom(): List<Locatie> {
        return locatieStore.getAllLocatiesFromRoom()
    }





    fun syncLocatiesFromFirebaseToRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            locatieStore.getAllLocaties().collect { locaties ->
                locaties.forEach { locatie ->
                    locatieStore.saveLocatieToRoom(locatie)
                }
            }
        }
    }


    fun syncLocatiesFromRoomToFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            val locaties = locatieStore.getAllLocatiesFromRoom()
            locaties.forEach { locatie ->
                locatieStore.saveLocatie(locatie)
            }
        }
    }

}
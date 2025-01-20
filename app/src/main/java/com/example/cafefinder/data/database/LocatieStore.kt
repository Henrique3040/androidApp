package com.example.cafefinder.data.database

import android.content.Context
import com.example.cafefinder.data.model.Locatie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Handles storage and retrieval of "Locatie" objects from Firebase Firestore and Room database.
 * This class bridges the gap between remote and local data sources.
 */
class LocatieStore (private val context: Context) {

    private val tag = "LocatieStore" // Tag for logging

    private val collection = "locaties2"  // Firestore collection name

    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val locatieDao = AppDatabase.getDatabase(context).locatieDao() // DAO for Room database


    /**
     * Fetches all locatie entries from the Room database.
     * @return List of [Locatie] objects stored locally.
     */
    suspend fun getAllLocatiesFromRoom(): List<Locatie> {
        return withContext(Dispatchers.IO) { // Use IO thread for database operation
            locatieDao.getAllLocaties()
        }
    }

    /**
     * Saves a new locatie to Firestore and Room database.
     * @param locatie The [Locatie] object to save.
     * @return A Flow emitting the document ID of the saved locatie or null if failed.
     */
    fun saveLocatie(locatie: Locatie): Flow<String?> {

        return callbackFlow {
            db.collection(collection)
                .document(locatie.id)
                .set(locatie.toHashMap())
                .addOnSuccessListener {
                    println(tag + "Locatie toegevoegd met id: ${locatie.id}")

                    CoroutineScope(Dispatchers.IO).launch {
                        locatieDao.insert(locatie) // Save to Room with the same ID
                    }

                    trySend(locatie.id)

                }
                .addOnFailureListener{ e ->
                    println(tag + "Fout bij het toevoegen van locatie: ${e.message}")
                    trySend(null)
                }

            awaitClose{}
        }
    }


    /**
     * Retrieves all locatie entries from Firestore in real-time.
     * @return A Flow emitting a list of [Locatie] objects.
     */
    fun getAllLocaties(): Flow<List<Locatie>> {
        return callbackFlow {
            val listenerRegistration = db.collection(collection)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        println(tag + "Error getting documents: ${exception.message}")
                        trySend(emptyList()) // Send empty list on error
                        return@addSnapshotListener
                    }

                    val locaties = snapshot?.documents?.mapNotNull { document ->
                        document.data?.toLocatie() // Map Firestore documents to Locatie objects
                    } ?: emptyList()

                    trySend(locaties)  // Emit the list of locaties
                }

            awaitClose { listenerRegistration.remove() } // Remove Firestore listener when Flow is closed
        }
    }


    /**
     * Deletes a locatie from Firestore and Room database.
     * @param locatieId The ID of the locatie to delete.
     * @return A Flow emitting a boolean indicating success or failure.
     */
    fun deleteLocatie(locatieId: String): Flow<Boolean> {

        return callbackFlow {
            if (locatieId.isEmpty()) {
                println("$tag Fout: Locatie-ID ontbreekt.")
                trySend(false)
                close()
                return@callbackFlow
            }

            db.collection(collection)
                .document(locatieId)
                .delete() // Delete locatie from Firestore
                .addOnSuccessListener {
                    println("$tag Locatie deleted from Firestore with ID: $locatieId")

                    // Delete from Room
                    CoroutineScope(Dispatchers.IO).launch {
                        locatieDao.deleteLocatieById(locatieId)
                        println("$tag Locatie deleted from Room with ID: $locatieId")
                        trySend(true) // Emit success after both deletions
                    }
                }
                .addOnFailureListener { e ->
                    println("$tag Fout bij het verwijderen van locatie: ${e.message}")
                    trySend(false)  // Emit failure
                }

            awaitClose {}
        }
    }


    /**
     * Converts a [Locatie] object to a HashMap for Firestore storage.
     */
    private fun Locatie.toHashMap(): HashMap<String, Any> {

        return hashMapOf(
            "id" to id,
            "name" to name,
            "address" to address
        )
    }

    /**
     * Converts a Firestore document data map to a [Locatie] object.
     */
    private fun Map<String, Any>.toLocatie(): Locatie {
        return Locatie(
            id = get("id") as String,
            name = get("name") as String,
            address = get("address") as String
        )

    }

}
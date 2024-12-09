package com.example.cafefinder

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocatieStore {

    private val tag = "LocatieStore"
    private val collection = "locaties"

    private val db = FirebaseFirestore.getInstance()

    fun saveLocatie(locatie: Locatie): Flow<String?> {
        return callbackFlow {
            db.collection(collection)
                .add(locatie.toHashMap())
                .addOnSuccessListener { document ->
                    println(tag + "Locatie toegevoegd met id: ${document.id}")

                    CoroutineScope(Dispatchers.IO).launch {
                    updateLocatie(locatie.copy(id = document.id)).collect{}
                    }

                    trySend(document.id)
                }
                .addOnFailureListener{ e ->
                    println(tag + "Fout bij het toevoegen van locatie: ${e.message}")
                    trySend(null)
                }

            awaitClose{}
        }
    }

    fun updateLocatie(locatie: Locatie): Flow<Boolean> {
        return callbackFlow {
            db.collection(collection)
                .document(locatie.id)
                .set(locatie.toHashMap())
                .addOnSuccessListener {
                    println(tag + "Locatie upgedate met id: ${locatie.id}")
                    trySend(true)
                }
                .addOnFailureListener{ e ->
                    println(tag + "Fout bij het toevoegen van locatie: ${e.message}")
                    trySend(false)
                }

            awaitClose{}
        }
    }

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
                        document.data?.toLocatie()
                    } ?: emptyList()

                    trySend(locaties)
                }

            awaitClose { listenerRegistration.remove() }
        }
    }

    fun getLocatie(address : String ): Flow<Locatie?> {
        return callbackFlow {
            db.collection(collection)
                .get()
                .addOnSuccessListener { result ->

                    var locatie: Locatie? = null
                    for (document in result) {
                        if (document.data["address"] == address) {
                            locatie = document.data.toLocatie()
                            println(tag + "Locatie gevonden met id: ${locatie.id}")
                            trySend(locatie)
                        }
                        }


                }
                .addOnFailureListener{ e ->
                    println(tag + "Fout bij het getting van locatie: ${e.message}")
                    trySend(null)
                }

            awaitClose{}
        }
    }


    private fun Locatie.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "address" to address
        )
    }

    private fun Map<String, Any>.toLocatie(): Locatie {
        return Locatie(
            id = get("id") as String,
            address = get("address") as String
        )

    }

}
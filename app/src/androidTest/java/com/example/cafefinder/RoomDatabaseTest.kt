package com.example.cafefinder

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.cafefinder.data.database.AppDatabase
import com.example.cafefinder.data.model.Locatie
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class RoomDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After
    fun teardown() {
        db.close()
    }


    @Test
    fun useDatabase() = runBlocking {
        val dao = db.locatieDao()

        // Voeg een locatie toe
        val locatie = Locatie("test", "Test Adress")
        dao.insert(locatie)

        // Controleer of de locatie correct wordt teruggegeven
        val savedLocatie = dao.getAllLocaties()
        val foundLocatie = savedLocatie.find { it.id == locatie.id }
        assertEquals(locatie.id, foundLocatie?.id)
    }

    @Test
    fun deleteLocatie() = runBlocking {
        val dao = db.locatieDao()

        // Voeg een locatie toe
        val locatie = Locatie("test", "Test Adress")
        dao.insert(locatie)

        assertEquals(1, dao.getAllLocaties().size)

        // Verwijder de locatie
        dao.delete(locatie)

        assertEquals(0, dao.getAllLocaties().size)

    }



}
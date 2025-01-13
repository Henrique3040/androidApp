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


/**
 * Unit tests for verifying the functionality of the Room database and DAO methods.
 * This test suite uses an in-memory database, which means all data will be stored in memory
 * and cleared after the tests complete.
 */
class RoomDatabaseTest {

    private lateinit var db: AppDatabase  // Reference to the in-memory database

    /**
     * Setup method executed before each test.
     * Creates an in-memory version of the database for testing.
     */
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    /**
     * Teardown method executed after each test.
     * Closes the in-memory database to release resources.
     */
    @After
    fun teardown() {
        db.close()
    }


    /**
     * Tests the basic functionality of adding and retrieving a locatie from the database.
     */
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

    /**
     * Tests the functionality of deleting a locatie from the database.
     */
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
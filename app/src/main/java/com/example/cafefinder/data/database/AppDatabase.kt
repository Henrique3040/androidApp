package com.example.cafefinder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.repository.LocatieDao

@Database(entities = [Locatie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locatieDao(): LocatieDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database2"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
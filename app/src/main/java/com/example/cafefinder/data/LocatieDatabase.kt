package com.example.cafefinder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Locatie::class], version = 1, exportSchema = false )
abstract class LocatieDatabase : RoomDatabase() {

    abstract fun locatieDao(): LocatieDao

    companion object {
        @Volatile
        private var INSTANCE: LocatieDatabase? = null

        fun getDatabase(context: Context):LocatieDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocatieDatabase::class.java,
                    "locatie_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }

    }

}
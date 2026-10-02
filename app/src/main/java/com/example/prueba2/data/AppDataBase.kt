package com.example.prueba2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Puerta::class, Objeto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun puertaDao(): PuertaDao
    abstract fun objetoDao(): ObjetoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prueba2_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
package com.example.agenda.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contato::class], version = 1)
abstract class ContatoDatabase : RoomDatabase() {

    abstract fun contatoDao(): ContatoDao

    companion object {
        @Volatile
        private var Instance: ContatoDatabase? = null

        fun getDatabase(context: Context): ContatoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ContatoDatabase::class.java, "item_database")
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
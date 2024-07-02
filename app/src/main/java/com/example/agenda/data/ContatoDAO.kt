package com.example.agenda.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContatoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contato: Contato)

    @Update
    fun update(contato: Contato)

    @Delete
    fun delete(contato: Contato)

    @Query("SELECT * FROM Contato")
    fun getAllContatos(): List<Contato>
}
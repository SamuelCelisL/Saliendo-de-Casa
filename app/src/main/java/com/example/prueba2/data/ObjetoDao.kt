package com.example.prueba2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface ObjetoDao {

    @Insert
    suspend fun insertarObjeto(objeto: Objeto)

    @Update
    suspend fun actualizarObjeto(objeto: Objeto)

    @Delete
    suspend fun eliminarObjeto(objeto: Objeto)

    @Query("SELECT * FROM objetos")
    suspend fun obtenerObjetos(): List<Objeto>
}
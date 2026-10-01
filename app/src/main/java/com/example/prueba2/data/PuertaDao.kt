package com.example.prueba2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface PuertaDao {

    @Insert
    suspend fun insertarPuerta(puerta: Puerta)

    @Update
    suspend fun actualizarPuerta(puerta: Puerta)

    @Delete
    suspend fun eliminarPuerta(puerta: Puerta)

    //@Query("SELECT * FROM puertas LIMIT 1")
    //suspend fun obtenerVivienda(): Puerta?

    @Query("SELECT * FROM puertas ")
    suspend fun obtenerPuertas(): List<Puerta>
}
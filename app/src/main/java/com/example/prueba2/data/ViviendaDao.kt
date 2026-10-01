package com.example.prueba2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface ViviendaDao {

    @Insert
    suspend fun insertarVivienda(vivienda: Vivienda)

    @Update
    suspend fun actualizarVivienda(vivienda: Vivienda)

    @Delete
    suspend fun eliminarVivienda(vivienda: Vivienda)

    @Query("SELECT * FROM viviendas LIMIT 1")
    suspend fun obtenerVivienda(): Vivienda?

    //@Query("SELECT * FROM viviendas ")
    //suspend fun obtenerVivienda(): Vivienda?
}
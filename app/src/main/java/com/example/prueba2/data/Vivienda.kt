package com.example.prueba2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viviendas")
data class Vivienda(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val latitud: Double,

    val longitud: Double
)
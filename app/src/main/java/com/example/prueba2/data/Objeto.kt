package com.example.prueba2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetos")
data class Objeto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String
)
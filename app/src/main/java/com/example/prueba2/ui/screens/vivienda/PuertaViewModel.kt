
package com.example.prueba2.ui.screens.vivienda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prueba2.data.Puerta
import com.example.prueba2.data.PuertaDao
import kotlinx.coroutines.launch

class PuertaViewModel(
    private val puertaDao: PuertaDao
) : ViewModel() {

    var puertas by mutableStateOf<List<Puerta>>(emptyList())
        private set

    init {
        cargarPuertas()
    }

    fun cargarPuertas() {
        viewModelScope.launch {
            puertas = puertaDao.obtenerPuertas()
        }
    }

    fun guardarPuerta(
        nombre: String,
        latitud: Double,
        longitud: Double
    ) {
        viewModelScope.launch {

            val nuevaPuerta = Puerta(
                nombre = nombre,
                latitud = latitud,
                longitud = longitud
            )

            puertaDao.insertarPuerta(nuevaPuerta)

            cargarPuertas()
        }
    }

    fun actualizarPuerta(puerta: Puerta) {
        viewModelScope.launch {
            puertaDao.actualizarPuerta(puerta)
            cargarPuertas()
        }
    }

    fun eliminarPuerta(puerta: Puerta) {
        viewModelScope.launch {
            puertaDao.eliminarPuerta(puerta)
            cargarPuertas()
        }
    }
}

class PuertaViewModelFactory(
    private val puertaDao: PuertaDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(PuertaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PuertaViewModel(puertaDao) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
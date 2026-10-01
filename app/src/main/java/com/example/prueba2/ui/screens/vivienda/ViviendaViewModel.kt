package com.example.prueba2.ui.screens.vivienda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba2.data.Vivienda
import com.example.prueba2.data.ViviendaDao
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ViviendaViewModel(
    private val viviendaDao: ViviendaDao
) : ViewModel() {

    var vivienda by mutableStateOf<Vivienda?>(null)
        private set

    init {
        cargarVivienda()
    }

    private fun cargarVivienda() {

        viewModelScope.launch {

            vivienda = viviendaDao.obtenerVivienda()
        }
    }

    fun guardarVivienda(
        nombre: String,
        latitud: Double,
        longitud: Double
    ) {
        viewModelScope.launch {

            val vivienda = Vivienda(
                nombre = nombre,
                latitud = latitud,
                longitud = longitud
            )

            viviendaDao.insertarVivienda(vivienda)
        }
    }

    fun actualizarVivienda(
        vivienda: Vivienda
    ) {
        viewModelScope.launch {
            viviendaDao.actualizarVivienda(vivienda)
            this@ViviendaViewModel.vivienda = vivienda
        }
    }

    fun eliminarVivienda(
        vivienda: Vivienda
    ) {
        viewModelScope.launch {
            viviendaDao.eliminarVivienda(vivienda)
            this@ViviendaViewModel.vivienda = null
        }
    }
}

class ViviendaViewModelFactory(
    private val viviendaDao: ViviendaDao
) : androidx.lifecycle.ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ViviendaViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ViviendaViewModel(viviendaDao) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
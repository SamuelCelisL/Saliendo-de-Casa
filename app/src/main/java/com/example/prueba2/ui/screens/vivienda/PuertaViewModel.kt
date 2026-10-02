
package com.example.prueba2.ui.screens.vivienda

import android.content.Context

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.prueba2.data.Puerta
import com.example.prueba2.data.PuertaDao
import com.example.prueba2.location.GeofenceManager

import kotlinx.coroutines.launch
import android.util.Log

class PuertaViewModel(
    private val puertaDao: PuertaDao, context: Context
) : ViewModel() {

    private val geofenceManager = GeofenceManager(context)

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

            // Registramos el geofence
            // La puerta nueva está activa por defecto
            val puertasActualizadas = puertaDao.obtenerPuertas()

            val puertaGuardada = puertasActualizadas
                .maxByOrNull { it.id }

            if (puertaGuardada != null && puertaGuardada.activa) {
                Log.d(
                    "GEOFENCE",
                    "Puerta guardada y activa. Registrando geofence: ${puertaGuardada.id}"
                )
                geofenceManager.registrarGeofence(puertaGuardada)
            }
        }
    }

    fun actualizarPuerta(puerta: Puerta) {
        viewModelScope.launch {
            Log.d(
                "GEOFENCE",
                "Actualizando puerta ${puerta.id}"
            )
            // Primero eliminamos el geofence anterior
            geofenceManager.eliminarGeofence(puerta)

            // Actualizamos la puerta en Room
            puertaDao.actualizarPuerta(puerta)

            // Actualizamos la lista mostrada en pantalla
            cargarPuertas()

            // Si la puerta sigue activa, registramos
            // un nuevo geofence con las nuevas coordenadas
            if (puerta.activa) {

                Log.d(
                    "GEOFENCE",
                    "La puerta ${puerta.id} sigue activa. Registrando nuevo geofence."
                )

                geofenceManager.registrarGeofence(puerta)

            } else {

                Log.d(
                    "GEOFENCE",
                    "La puerta ${puerta.id} está desactivada. No se registra geofence."
                )
            }
        }
    }

    fun eliminarPuerta(puerta: Puerta) {
        viewModelScope.launch {
            Log.d(
                "GEOFENCE",
                "Eliminando puerta ${puerta.id}"
            )
            geofenceManager.eliminarGeofence(puerta)
            puertaDao.eliminarPuerta(puerta)
            cargarPuertas()
        }
    }

    fun cambiarEstadoPuerta(
        puerta: Puerta,
        activa: Boolean
    ) {
        viewModelScope.launch {

            val puertaActualizada = puerta.copy(
                activa = activa
            )

            puertaDao.actualizarPuerta(puertaActualizada)

            cargarPuertas()

            if (activa) {
                // La puerta fue activada
                geofenceManager.registrarGeofence(
                    puertaActualizada
                )
            } else {
                geofenceManager.eliminarGeofence(
                    puertaActualizada
                )
            }
        }
    }
}

class PuertaViewModelFactory(
    private val puertaDao: PuertaDao,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(PuertaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PuertaViewModel(puertaDao, context) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
package com.example.prueba2.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.example.prueba2.data.AppDatabase
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val geofencingEvent =
            GeofencingEvent.fromIntent(intent)

        if (geofencingEvent == null) {

            Log.e(
                "GEOFENCE",
                "No se pudo obtener el evento"
            )

            return
        }

        if (geofencingEvent.hasError()) {

            Log.e(
                "GEOFENCE",
                "Error en el geofence: ${geofencingEvent.errorCode}"
            )

            return
        }

        val transition =
            geofencingEvent.geofenceTransition

        if (
            transition !=
            Geofence.GEOFENCE_TRANSITION_ENTER
        ) {
            return
        }

        val geofences =
            geofencingEvent.triggeringGeofences

        if (geofences.isNullOrEmpty()) {
            return
        }

        // Indicamos que el BroadcastReceiver
        // realizará trabajo de forma asíncrona
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val database =
                    AppDatabase.obtenerDatabase(context)

                val objetos =
                    database.objetoDao()
                        .obtenerObjetos()

                for (geofence in geofences) {

                    Log.d(
                        "GEOFENCE",
                        "Entraste en la zona: ${geofence.requestId}"
                    )

                    Log.d(
                        "GEOFENCE",
                        "Objetos registrados: ${objetos.size}"
                    )

                    for (objeto in objetos) {

                        Log.d(
                            "GEOFENCE",
                            "Objeto: ${objeto.nombre}"
                        )
                    }
                }

            } finally {

                pendingResult.finish()
            }
        }
    }
}
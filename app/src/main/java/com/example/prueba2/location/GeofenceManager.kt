package com.example.prueba2.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

import com.example.prueba2.data.Puerta
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceManager(
    private val context: Context
) {

    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {

        val intent = Intent(
            context,
            GeofenceBroadcastReceiver::class.java
        )

        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun registrarGeofence(puerta: Puerta) {

        Log.d(
            "GEOFENCE",
            "Iniciando registro de geofence para puerta: ${puerta.id}"
        )

        val geofence = Geofence.Builder()
            .setRequestId("puerta_${puerta.id}")
            .setCircularRegion(
                puerta.latitud,
                puerta.longitud,
                50f
            )
            .setExpirationDuration(
                Geofence.NEVER_EXPIRE
            )
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER
            )
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(
                GeofencingRequest.INITIAL_TRIGGER_ENTER
            )
            .addGeofence(geofence)
            .build()

        geofencingClient
            .addGeofences(
                geofencingRequest,
                geofencePendingIntent
            )
            .addOnSuccessListener {
                Log.d(
                    "GEOFENCE",
                    "Geofence registrado correctamente: puerta_${puerta.id}"
                )
            }
            .addOnFailureListener{ exception ->
                Log.e(
                    "GEOFENCE",
                    "Error al registrar geofence: ${exception.message}"
                )
            }
    }

    @SuppressLint("MissingPermission")
    fun eliminarGeofence(puerta: Puerta) {

        geofencingClient
            .removeGeofences(
                listOf("puerta_${puerta.id}")
            )
            .addOnSuccessListener {

                Log.d(
                    "GEOFENCE",
                    "Geofence eliminado correctamente: puerta_${puerta.id}"
                )
            }
            .addOnFailureListener { exception ->

                Log.e(
                    "GEOFENCE",
                    "Error al eliminar geofence: ${exception.message}"
                )
            }
    }
}
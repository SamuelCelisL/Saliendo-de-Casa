package com.example.prueba2.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent == null) {
            Log.e("GEOFENCE", "No se pudo obtener el evento")
            return
        }

        if (geofencingEvent.hasError()) {
            Log.e(
                "GEOFENCE",
                "Error en el geofence: ${geofencingEvent.errorCode}"
            )
            return
        }

        val transition = geofencingEvent.geofenceTransition

        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            val geofences = geofencingEvent.triggeringGeofences

            if (!geofences.isNullOrEmpty()) {

                for (geofence in geofences) {

                    Log.d(
                        "GEOFENCE",
                        "Entraste en la zona: ${geofence.requestId}"
                    )
                }
            }
        }
    }
}
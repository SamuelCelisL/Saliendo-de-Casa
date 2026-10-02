package com.example.prueba2.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {

    private const val CHANNEL_ID = "recordatorio_objetos"

    fun crearCanalNotificaciones(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nombre = "Recordatorios"

            val descripcion =
                "Notificaciones para recordar objetos al salir de casa"

            val importancia =
                NotificationManager.IMPORTANCE_HIGH

            val canal = NotificationChannel(
                CHANNEL_ID,
                nombre,
                importancia
            )

            canal.description = descripcion

            val notificationManager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            notificationManager.createNotificationChannel(
                canal
            )
        }
    }

    fun obtenerChannelId(): String {
        return CHANNEL_ID
    }
}
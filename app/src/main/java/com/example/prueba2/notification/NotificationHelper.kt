package com.example.prueba2.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.prueba2.R

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

            notificationManager.createNotificationChannel(canal)
        }
    }

    fun mostrarRecordatorio(
        context: Context,
        objetos: List<String>
    ) {

        // Android 13 o superior necesita permiso
        // para mostrar notificaciones.
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val listaObjetos = objetos.joinToString(
            separator = "\n"
        ) {
            "• $it"
        }

        val notificacion = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Antes de salir")
            .setContentText("Recuerda llevar tus objetos")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Recuerda llevar:\n\n$listaObjetos"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                1001,
                notificacion
            )
    }

    fun obtenerChannelId(): String {
        return CHANNEL_ID
    }
}
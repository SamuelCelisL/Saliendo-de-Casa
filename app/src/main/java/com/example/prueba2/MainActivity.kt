package com.example.prueba2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.prueba2.ui.screens.welcome.WelcomeScreen
import com.example.prueba2.ui.screens.menu.MenuScreen
import com.example.prueba2.ui.theme.Prueba2Theme
import com.example.prueba2.notification.NotificationHelper
import android.Manifest
import android.os.Build

import androidx.activity.result.contract.ActivityResultContracts
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class MainActivity : ComponentActivity() {

    private val solicitarPermisoNotificaciones =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permitido ->

            if (permitido) {
                Log.d(
                    "NOTIFICACION",
                    "Permiso de notificaciones concedido"
                )
            } else {
                Log.d(
                    "NOTIFICACION",
                    "Permiso de notificaciones DENEGADO"
                )
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.crearCanalNotificaciones(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val permiso =
                checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                )

            Log.d(
                "NOTIFICACION",
                "Permiso actual: $permiso"
            )

            Log.d(
                "NOTIFICACION",
                "PERMISSION_GRANTED: ${PackageManager.PERMISSION_GRANTED}"
            )

            val notificacionesHabilitadas =
                NotificationManagerCompat
                    .from(this)
                    .areNotificationsEnabled()

            Log.d(
                "NOTIFICACION",
                "Notificaciones habilitadas: $notificacionesHabilitadas"
            )

            if (
                permiso != PackageManager.PERMISSION_GRANTED
            ) {

                Log.d(
                    "NOTIFICACION",
                    "Solicitando permiso POST_NOTIFICATIONS"
                )

                solicitarPermisoNotificaciones.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )

            } else {

                Log.d(
                    "NOTIFICACION",
                    "Permiso de notificaciones ya concedido"
                )
            }
        }

        setContent {
            Prueba2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "welcome"
                    ) {

                        composable("welcome") {

                            WelcomeScreen(
                                onStartClick = {
                                    navController.navigate("menu")
                                }
                            )
                        }

                        composable("menu") {
                                MenuScreen(

                                )
                        }
                    }
                }
            }
        }
    }
}

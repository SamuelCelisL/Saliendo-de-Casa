package com.example.prueba2.ui.screens.vivienda

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
//import android.annotation.SuppressLint
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViviendaScreen() {

    var registrarVivienda by remember {
        mutableStateOf(false)
    }

    var nombreVivienda by remember {
        mutableStateOf("")
    }

    var latitud by remember {
        mutableStateOf<Double?>(null)
    }

    var longitud by remember {
        mutableStateOf<Double?>(null)
    }

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun obtenerUbicacion() {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted && !coarseLocationGranted) {
            return
        }

        val cancellationTokenSource = CancellationTokenSource()

        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0)
            .build()

        fusedLocationClient.getCurrentLocation(
            locationRequest,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->

            if (location != null) {
                latitud = location.latitude
                longitud = location.longitude
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val fineLocationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val coarseLocationGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            obtenerUbicacion()
        }
    }

    if (!registrarVivienda){

        Column {

            Text(
                text = "Vivienda"
            )

            Text(
                text = "No tienes una vivienda registrada.",
                modifier = Modifier.padding(top = 16.dp)
            )

            Button(
                onClick = {
                    registrarVivienda = true
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Registrar vivienda")
            }
        }
    } else {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    registrarVivienda = false
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("X")
            }

            Text(
                text = "Registrar vivienda"
            )

            OutlinedTextField(
                value = nombreVivienda,
                onValueChange = {
                    nombreVivienda = it
                },
                label = {
                    Text("Nombre de la vivienda")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Ubicación de la puerta principal"
            )

            Text(
                text = "Dirígete a la puerta principal de tu vivienda " +
                        "y permanece allí antes de tomar las coordenadas."
            )

            Button(
                onClick = {

                    val fineLocationGranted =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                    val coarseLocationGranted =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                    if (fineLocationGranted || coarseLocationGranted) {

                        obtenerUbicacion()

                    } else {

                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar coordenadas")
            }

            Text(
                text = "Latitud: ${latitud ?: "--"}"
            )

            Text(
                text = "Longitud: ${longitud ?: "--"}"
            )

            Button(
                onClick = {
                    // Aquí guardaremos la vivienda
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar vivienda")
            }
        }
    }
}
package com.example.prueba2.ui.screens.vivienda

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch

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
import androidx.core.content.ContextCompat

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba2.data.AppDatabase
import com.example.prueba2.data.Puerta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuertaScreen() {

    var registrarPuerta by remember {
        mutableStateOf(false)
    }

    var nombrePuerta by remember {
        mutableStateOf("")
    }

    var latitud by remember {
        mutableStateOf<Double?>(null)
    }

    var longitud by remember {
        mutableStateOf<Double?>(null)
    }

    var editarPuerta by remember {
        mutableStateOf(false)
    }

    var puertaSeleccionada by remember {
        mutableStateOf<Puerta?>(null)
    }

    val context = LocalContext.current

    val database = remember {
        AppDatabase.obtenerDatabase(context)
    }


    val puertaViewModel: PuertaViewModel = viewModel(
        factory = PuertaViewModelFactory(
            database.puertaDao(), context
        )
    )

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

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

    fun reiniciarValores(){
        editarPuerta = false
        registrarPuerta = false
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

    if (!registrarPuerta && !editarPuerta) {

        Column {

            Text(
                text = "Puertas Registradas"
            )

            if (puertaViewModel.puertas.isEmpty()) {

                Text(
                    text = "No tienes puertas registradas.",
                    modifier = Modifier.padding(top = 16.dp)
                )

            }
            else {

                puertaViewModel.puertas.forEach { puerta ->

                Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Nombre: ${puerta.nombre}"
                            )

                            Text(
                                text = "Latitud: ${puerta.latitud}"
                            )

                            Text(
                                text = "Longitud: ${puerta.longitud}"
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = if (puerta.activa) {
                                        "Puerta activa"
                                    } else {
                                        "Puerta desactivada"
                                    }
                                )

                                Switch(
                                    checked = puerta.activa,
                                    onCheckedChange = { nuevoEstado ->

                                        puertaViewModel.cambiarEstadoPuerta(
                                            puerta = puerta,
                                            activa = nuevoEstado
                                        )
                                    }
                                )
                            }

                            Button(
                                onClick = {
                                    puertaSeleccionada = puerta
                                    nombrePuerta = puerta.nombre
                                    latitud = puerta.latitud
                                    longitud = puerta.longitud

                                    editarPuerta = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Editar")
                            }

                            Button(
                                onClick = {
                                    puertaSeleccionada = puerta
                                    mostrarDialogoEliminar = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {

                    nombrePuerta = ""
                    latitud = null
                    longitud = null

                    editarPuerta = false
                    registrarPuerta = true
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Registrar puerta")
            }
        }
    }
    else {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    reiniciarValores()
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("X")
            }

            Text(
                text = if (editarPuerta) "Editar vivienda" else "Registrar vivienda"
            )

            OutlinedTextField(
                value = nombrePuerta,
                onValueChange = {
                    nombrePuerta = it
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

                    if (
                        nombrePuerta.isNotBlank() &&
                        latitud != null &&
                        longitud != null
                    ) {

                        if (editarPuerta) {

                            puertaViewModel.actualizarPuerta(
                                Puerta(
                                    id = puertaSeleccionada!!.id,
                                    nombre = nombrePuerta,
                                    latitud = latitud!!,
                                    longitud = longitud!!,
                                    activa = puertaSeleccionada!!.activa
                                )
                            )

                            editarPuerta = false

                        } else {

                            puertaViewModel.guardarPuerta(
                                nombre = nombrePuerta,
                                latitud = latitud!!,
                                longitud = longitud!!
                            )

                            registrarPuerta = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (editarPuerta) {
                        "Guardar cambios"
                    } else {
                        "Guardar vivienda"
                    }
                )
            }
        }
    }
    if (mostrarDialogoEliminar && puertaSeleccionada != null) {

        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
                puertaSeleccionada = null
            },

            title = {
                Text("Eliminar puerta")
            },

            text = {
                Text("¿Estás seguro de que deseas eliminar "+ "\"${puertaSeleccionada?.nombre}\"?")
            },

            confirmButton = {

                Button(
                    onClick = {

                        val puertaAEliminar = puertaSeleccionada

                        if (puertaAEliminar != null) {

                            puertaViewModel.eliminarPuerta(puertaAEliminar)

                            mostrarDialogoEliminar = false
                            puertaSeleccionada = null
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        mostrarDialogoEliminar = false
                        puertaSeleccionada = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

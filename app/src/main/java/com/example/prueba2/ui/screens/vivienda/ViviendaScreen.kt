package com.example.prueba2.ui.screens.vivienda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViviendaScreen() {

    var registrarVivienda by remember {
        mutableStateOf(false)
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
        var nombreVivienda by remember {
            mutableStateOf("")
        }

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
                    // Aquí obtendremos las coordenadas
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar coordenadas")
            }

            Text(
                text = "Latitud: --"
            )

            Text(
                text = "Longitud: --"
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
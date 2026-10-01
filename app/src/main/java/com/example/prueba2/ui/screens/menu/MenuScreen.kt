package com.example.prueba2.ui.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prueba2.ui.screens.vivienda.PuertaScreen

@Composable
fun MenuScreen() {

    var opcionSeleccionada by remember {
        mutableStateOf("vivienda")
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // MENÚ LATERAL
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(150.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                )
                .padding(16.dp),

            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Salida de Casa",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 25.dp)
            )

            Button(
                onClick = {
                    opcionSeleccionada = "vivienda"
                },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text("Vivienda")
            }

            Button(
                onClick = {
                    opcionSeleccionada = "objetos"
                },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text("Objetos")
            }

            Button(
                onClick = {
                    opcionSeleccionada = "configuracion"
                },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text("Opción 2")
            }
        }

        // CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            when(opcionSeleccionada){
                "vivienda" -> {
                    PuertaScreen()
                }
                "objetos" -> {
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Selecciona una opción del menú.",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                "configuracion" -> {
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Selecciona una opción del menú.",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
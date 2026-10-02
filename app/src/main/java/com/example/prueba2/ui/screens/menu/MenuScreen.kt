package com.example.prueba2.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prueba2.ui.screens.objeto.ObjetoScreen
import com.example.prueba2.ui.screens.vivienda.PuertaScreen

@Composable
fun MenuScreen() {

    var opcionSeleccionada by remember {
        mutableStateOf("puertas")
    }

    var menuAbierto by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // BARRA SUPERIOR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Salida de Casa",
                style = MaterialTheme.typography.titleLarge
            )

            Column {

                Button(
                    onClick = {
                        menuAbierto = true
                    }
                ) {
                    Text("Menú")
                }

                DropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = {
                        menuAbierto = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Puertas")
                        },
                        onClick = {
                            opcionSeleccionada = "puertas"
                            menuAbierto = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Objetos")
                        },
                        onClick = {
                            opcionSeleccionada = "objetos"
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        // CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {

            when (opcionSeleccionada) {

                "puertas" -> {
                    PuertaScreen()
                }

                "objetos" -> {
                    ObjetoScreen()
                }
            }
        }
    }
}


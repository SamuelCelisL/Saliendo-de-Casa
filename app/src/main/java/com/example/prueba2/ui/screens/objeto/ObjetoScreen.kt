package com.example.prueba2.ui.screens.objeto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba2.data.AppDatabase
import com.example.prueba2.data.Objeto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjetoScreen() {

    var registrarObjeto by remember {
        mutableStateOf(false)
    }

    var editarObjeto by remember {
        mutableStateOf(false)
    }

    var nombreObjeto by remember {
        mutableStateOf("")
    }

    var objetoSeleccionado by remember {
        mutableStateOf<Objeto?>(null)
    }

    var mostrarDialogoEliminar by remember {
        mutableStateOf(false)
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    val database = remember {
        AppDatabase.obtenerDatabase(context)
    }

    val objetoViewModel: ObjetoViewModel = viewModel(
        factory = ObjetoViewModelFactory(
            database.objetoDao()
        )
    )

    fun cerrarFormulario() {

        registrarObjeto = false
        editarObjeto = false

        nombreObjeto = ""

        objetoSeleccionado = null
    }

    if (!registrarObjeto && !editarObjeto) {

        Column {

            Text(
                text = "Objetos a recordar"
            )

            if (objetoViewModel.objetos.isEmpty()) {

                Text(
                    text = "No tienes objetos registrados.",
                    modifier = Modifier.padding(top = 16.dp)
                )

            } else {
                LazyColumn( modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = objetoViewModel.objetos,
                        key = { objeto -> objeto.id }
                    ) { objeto ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text( text = "Nombre: ${objeto.nombre}"
                                )
                                Button(
                                    onClick = {
                                        objetoSeleccionado = objeto
                                        nombreObjeto = objeto.nombre
                                        editarObjeto = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Text("Editar")
                                }
                                Button(
                                    onClick = {
                                        objetoSeleccionado = objeto
                                        mostrarDialogoEliminar = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
            Button(
                onClick = {

                    nombreObjeto = ""
                    objetoSeleccionado = null

                    editarObjeto = false
                    registrarObjeto = true
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Registrar objeto")
            }
        }

    } else {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    onClick = {
                        cerrarFormulario()
                    },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("X")
                }
            }

            Text(
                text = if (editarObjeto) {
                    "Editar objeto"
                } else {
                    "Registrar objeto"
                }
            )

            OutlinedTextField(
                value = nombreObjeto,
                onValueChange = {
                    nombreObjeto = it
                },
                label = {
                    Text("Nombre del objeto")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    if (nombreObjeto.isNotBlank()) {

                        if (editarObjeto && objetoSeleccionado != null) {

                            val objeto = objetoSeleccionado!!

                            objetoViewModel.actualizarObjeto(
                                Objeto(
                                    id = objeto.id,
                                    nombre = nombreObjeto
                                )
                            )

                        } else {

                            objetoViewModel.guardarObjeto(
                                nombre = nombreObjeto
                            )
                        }

                        cerrarFormulario()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (editarObjeto) {
                        "Guardar cambios"
                    } else {
                        "Guardar objeto"
                    }
                )
            }
        }
    }

    if (mostrarDialogoEliminar && objetoSeleccionado != null) {

        val objeto = objetoSeleccionado

        AlertDialog(

            onDismissRequest = {
                mostrarDialogoEliminar = false
                objetoSeleccionado = null
            },

            title = {
                Text("Eliminar objeto")
            },

            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "\"${objeto?.nombre}\"?"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        if (objeto != null) {

                            objetoViewModel.eliminarObjeto(objeto)

                            mostrarDialogoEliminar = false
                            objetoSeleccionado = null
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
                        objetoSeleccionado = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
package com.example.prueba2.ui.screens.objeto

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prueba2.data.Objeto
import com.example.prueba2.data.ObjetoDao
import kotlinx.coroutines.launch

class ObjetoViewModel(
    private val objetoDao: ObjetoDao
) : ViewModel() {

    var objetos by mutableStateOf<List<Objeto>>(emptyList())
        private set

    init {
        cargarObjetos()
    }

    fun cargarObjetos() {

        viewModelScope.launch {

            objetos = objetoDao.obtenerObjetos()
        }
    }

    fun guardarObjeto(
        nombre: String
    ) {

        viewModelScope.launch {

            val nuevoObjeto = Objeto(
                nombre = nombre
            )

            objetoDao.insertarObjeto(nuevoObjeto)

            cargarObjetos()
        }
    }

    fun actualizarObjeto(
        objeto: Objeto
    ) {

        viewModelScope.launch {

            objetoDao.actualizarObjeto(objeto)

            cargarObjetos()
        }
    }

    fun eliminarObjeto(
        objeto: Objeto
    ) {

        viewModelScope.launch {

            objetoDao.eliminarObjeto(objeto)

            cargarObjetos()
        }
    }
}

class ObjetoViewModelFactory(
    private val objetoDao: ObjetoDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ObjetoViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ObjetoViewModel(objetoDao) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
package com.example.closetfit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.model.Producto
import com.example.closetfit.repository.ApiProducto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val apiProductos: ApiProducto = ApiProducto()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiProductos.obtenerProductos()
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                } else {
                    _mensaje.value = "Error al cargar productos: ${response.code()}"
                    _productos.value = emptyList()
                }
            } catch (e: Exception) {
                _mensaje.value = "Error de conexión: ${e.message}"
                _productos.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun buscarProductoPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }

    fun filtrarPorCategoria(categoria: String): List<Producto> {
        if (categoria.isEmpty() || categoria == "Todos") {
            return _productos.value
        }
        return _productos.value.filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
    }

    fun buscarProductos(query: String): List<Producto> {
        if (query.isEmpty()) {
            return _productos.value
        }
        return _productos.value.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true) ||
                    it.categoria.contains(query, ignoreCase = true)
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = ""
    }
}
package com.example.closetfit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.model.Producto
import com.example.closetfit.repository.ApiProducto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ApiProductoViewModel(app: Application) : AndroidViewModel(app) {
    private val apiProductos = ApiProducto()

    private val _allProductos = MutableStateFlow<List<Producto>>(emptyList())
    val allProductos: StateFlow<List<Producto>> = _allProductos.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = apiProductos.obtenerProductos()
            if (response.isSuccessful) {
                _allProductos.value = response.body() ?: emptyList()
            } else {
                _mensaje.value = "Error al cargar productos: ${response.code()}"
                _allProductos.value = emptyList()
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
            _allProductos.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun obtenerProductoPorId(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = apiProductos.obtenerProducto(id)
            if (response.isSuccessful) {
                // Puedes manejar el producto individual si necesitas
            } else {
                _mensaje.value = "Error al obtener producto: ${response.code()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun agregarProducto(
        nombre: String,
        categoria: String,
        talla: String,
        precio: Double,
        urlImagen: String,
        stock: Int,
        descripcion: String
    ) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val producto = Producto(
                id = 0,
                nombre = nombre,
                categoria = categoria,
                talla = talla,
                precio = precio,
                urlImagen = urlImagen,
                stock = stock,
                descripcion = descripcion
            )
            val response = apiProductos.agregarProducto(producto)
            if (response.isSuccessful) {
                _mensaje.value = "Producto agregado correctamente"
                cargarProductos()
            } else {
                _mensaje.value = "Error al agregar producto: ${response.code()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun eliminarProducto(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = apiProductos.eliminarProducto(id)
            if (response.isSuccessful) {
                _mensaje.value = "Producto eliminado correctamente"
                cargarProductos()
            } else {
                _mensaje.value = "Error al eliminar producto: ${response.code()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun filtrarPorCategoria(categoria: String) {
        if (categoria.isEmpty() || categoria == "Todos") {
            // No filtrar, mostrar todos
            return
        }
        _allProductos.value = _allProductos.value.filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
    }

    fun buscarProductos(query: String) {
        if (query.isEmpty()) {
            cargarProductos()
            return
        }
        _allProductos.value = _allProductos.value.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true) ||
                    it.categoria.contains(query, ignoreCase = true)
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = ""
    }
}
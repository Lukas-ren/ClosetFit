package com.example.closetfit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.collections.emptyList
import com.example.closetfit.model.Carrito

class CarritoViewmodel : ViewModel() {
    private val _carrito = MutableStateFlow<List<Carrito>>(emptyList())
    val carrito: StateFlow<List<Carrito>> = _carrito
    val total: StateFlow<Int> = carrito.map { items ->
        items.sumOf { item ->
            item.precio * item.cantidad
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
    fun agregarProducto(item: Carrito) {
        _carrito.update { current ->
            val existente = current.find { it.id == item.id }
            if (existente != null) {
                current.map {
                    if (it.id == item.id) it.copy(cantidad = it.cantidad + 1) else it
                }
            } else {
                current + item
            }
        }
    }
    fun quitarProducto(id: Int) {
        _carrito.update { current ->
            current.filterNot { it.id == id }
        }
    }
    fun agregarSiNoExiste(producto: Carrito) {
        _carrito.update { current ->
            val existe = current.any { it.id == producto.id }
            if (!existe) {
                current + producto.copy(cantidad = 1)
            } else {
                current
            }
        }
    }
    fun disminuirCantidad(id: Int) {
        _carrito.update { current ->
            current.mapNotNull {
                if (it.id == id) {
                    if (it.cantidad > 1) it.copy(cantidad = it.cantidad - 1) else null
                } else it
            }
        }
    }
    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }
}
package com.example.closetfit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.closetfit.model.ItemCarrito
import com.example.closetfit.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarritoViewModel : ViewModel() {
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    private val _cantidadTotal = MutableStateFlow(0)
    val cantidadTotal: StateFlow<Int> = _cantidadTotal.asStateFlow()

    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        val carritoActual = _carrito.value.toMutableList()

        val itemExistente = carritoActual.find { it.id == producto.id }

        if (itemExistente != null) {
            // Ya existe, aumentar cantidad
            val nuevaCantidad = itemExistente.cantidad + cantidad
            if (nuevaCantidad <= producto.stock) {
                carritoActual[carritoActual.indexOf(itemExistente)] = itemExistente.copy(
                    cantidad = nuevaCantidad
                )
            }
        } else {
            // Agregar nuevo item
            if (cantidad <= producto.stock) {
                carritoActual.add(
                    ItemCarrito(
                        id = producto.id,
                        nombre = producto.nombre,
                        categoria = producto.categoria,
                        talla = producto.talla,
                        precio = producto.precio,
                        urlImagen = producto.urlImagen,
                        cantidad = cantidad,
                        stockDisponible = producto.stock
                    )
                )
            }
        }

        _carrito.value = carritoActual
        calcularTotal()
    }

    fun aumentarCantidad(productoId: Int) {
        val carritoActual = _carrito.value.toMutableList()
        val item = carritoActual.find { it.id == productoId }

        if (item != null && item.cantidad < item.stockDisponible) {
            carritoActual[carritoActual.indexOf(item)] = item.copy(
                cantidad = item.cantidad + 1
            )
            _carrito.value = carritoActual
            calcularTotal()
        }
    }

    fun disminuirCantidad(productoId: Int) {
        val carritoActual = _carrito.value.toMutableList()
        val item = carritoActual.find { it.id == productoId }

        if (item != null) {
            if (item.cantidad > 1) {
                carritoActual[carritoActual.indexOf(item)] = item.copy(
                    cantidad = item.cantidad - 1
                )
            } else {
                carritoActual.remove(item)
            }
            _carrito.value = carritoActual
            calcularTotal()
        }
    }

    fun quitarProducto(productoId: Int) {
        _carrito.value = _carrito.value.filter { it.id != productoId }
        calcularTotal()
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
        calcularTotal()
    }

    private fun calcularTotal() {
        val total = _carrito.value.sumOf { it.subtotal }
        _total.value = total
        _cantidadTotal.value = _carrito.value.sumOf { it.cantidad }
    }
}
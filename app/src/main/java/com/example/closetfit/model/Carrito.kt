package com.example.closetfit.model

data class ItemCarrito(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val talla: String,
    val precio: Double,
    val urlImagen: String,
    val cantidad: Int,
    val stockDisponible: Int
) {
    val subtotal: Double
        get() = precio * cantidad
}
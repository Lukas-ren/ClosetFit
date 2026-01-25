package com.example.closetfit.model

data class Carrito (
    val id : Int,
    val nombre: String,
    val precio: Int,
    var cantidad: Int = 1,
    val imagen: String = ""
) {
    val subtotal: Int
        get() = precio * cantidad
}
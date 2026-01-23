package com.example.closetfit.model

data class Producto (
    val id: Int,
    val nombre: String,
    val categoria: String,
    val talla: String,
    val precio: Int,
    val imagen: String,
    val stock: Int,
)
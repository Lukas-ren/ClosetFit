package com.example.closetfit.model

import com.google.gson.annotations.SerializedName

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val talla: String,
    val precio: Double,
    @SerializedName("urlImagen")
    val urlImagen: String,
    val stock: Int,
    val descripcion: String
)
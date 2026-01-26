package com.example.closetfit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Producto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // <--- CAMBIO AQUÍ
    val nombre: String,
    val categoria: String,
    val talla: String,
    val precio: Int,
    val imagen: String,
    val stock: Int
)

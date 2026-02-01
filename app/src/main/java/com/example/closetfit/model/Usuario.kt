package com.example.closetfit.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val rol: String,
    val correo: String,
    @SerializedName("contraseña")
    val contraseña: String,
    val run: String,
    val direccion: String
)
package com.example.closetfit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val run: String?,
    val direccion: String?
) {
    constructor(nombre: String, email: String, password: String) : this(0, nombre, email, password, null, null)
}
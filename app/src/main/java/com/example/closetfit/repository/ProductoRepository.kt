package com.example.closetfit.repository

import android.content.Context
import android.util.Log
import com.example.closetfit.model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductoRepository {

    fun obtenerProducto(context: Context, filename: String = "producto.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson<List<Producto>>(json, listType) ?: emptyList()
        } catch (e: Exception) {
            Log.e("REPO_ERROR", "¡Fallo crítico en la carga del JSON!", e)
            emptyList()
        }
    }
    fun agregarProducto(producto: Producto) {
        println("Repositorio: Agregando el producto ${producto.nombre}...")
        println("Datos a guardar: ID=${producto.id}, Precio=${producto.precio}, Stock=${producto.stock}")
    }

    fun editarProducto(producto: Producto) {
        println("Repositorio: Editando el producto ${producto.nombre}")
    }

    fun eliminarProducto(producto: Producto) {
        println("Repositorio: Eliminando el producto ${producto.nombre} con ID ${producto.id}")
    }
}
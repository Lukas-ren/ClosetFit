package com.example.closetfit.repository

import com.example.closetfit.model.Producto
import com.example.closetfit.repository.RetrofitClient
import retrofit2.Response

class APIProductos {

    //Obtener todos los productos
    suspend fun obtenerProductos(): Response<List<Producto>> {
        return RetrofitClient.apiServiceProducto.getProductos()
    }

    //Obtener un producto por su id
    suspend fun obtenerProducto(id: Int): Response<Producto> {
        return RetrofitClient.apiServiceProducto.getProducto(id)
    }

    //Agregar un producto
    suspend fun agregarProducto(producto: Producto): Response<Producto> {
        return RetrofitClient.apiServiceProducto.createProducto(producto)
    }

    //Eliminar un producto
    suspend fun eliminarProducto(id: Int): Response<Void> {
        return RetrofitClient.apiServiceProducto.deleteProducto(id)
    }
}
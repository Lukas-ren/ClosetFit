package com.example.closetfit.interfaces

import com.example.closetfit.model.Producto
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceProducto {

    @GET("api/producto")
    suspend fun getProductos(): Response<List<Producto>>

    @GET("api/producto/{id}")
    suspend fun getProducto(@Path("id") id: Int): Response<Producto>

    @POST("api/producto")
    suspend fun createProducto(@Body producto: Producto): Response<Producto>

    @DELETE("api/producto/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Void>
}
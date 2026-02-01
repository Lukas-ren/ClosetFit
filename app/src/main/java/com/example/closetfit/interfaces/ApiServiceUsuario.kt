package com.example.closetfit.interfaces

import com.example.closetfit.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceUsuario {

    @GET("api/usuario")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("api/usuario/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<Usuario>

    @POST("api/usuario")
    suspend fun createUsuario(@Body usuario: Usuario): Response<Usuario>

    @DELETE("api/usuario/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void>
}
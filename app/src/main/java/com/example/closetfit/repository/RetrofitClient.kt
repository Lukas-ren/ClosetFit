package com.example.closetfit.repository

import com.example.closetfit.interfaces.ApiServiceUsuario
import com.example.closetfit.interfaces.ApiServiceProducto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://044gnc85-8081.brs.devtunnels.ms/"

    val apiServiceUsuario: ApiServiceUsuario by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceUsuario::class.java)
    }

    val apiServiceProducto: ApiServiceProducto by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceProducto::class.java)
    }
}
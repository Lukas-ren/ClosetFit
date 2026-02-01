package com.example.myapplication.repository

import com.example.closetfit.model.Usuario
import com.example.closetfit.repository.RetrofitClient
import retrofit2.Response

class APIUsuarios {

    //Obtener todos los usuarios
    suspend fun obtenerUsuarios():Response<List<Usuario>> {
        return RetrofitClient.apiServiceUsuario.getUsuarios()

    }
    //Obtener un usuario por su id
    suspend fun obtenerUsuario(id: Int): Response<Usuario> {
        return RetrofitClient.apiServiceUsuario.getUsuario(id)
    }

    //Agregar un usuario
    suspend fun agregarUsuario(usuario: Usuario): Response<Usuario> {
        return RetrofitClient.apiServiceUsuario.createUsuario(usuario)
    }

    //Eliminar un usuario
    suspend fun eliminarUsuario(id: Int): Response<Void> {
        return RetrofitClient.apiServiceUsuario.deleteUsuario(id)
    }

}

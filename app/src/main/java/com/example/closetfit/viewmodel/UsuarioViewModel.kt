package com.example.closetfit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.model.Usuario
import com.example.closetfit.repository.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application) : AndroidViewModel(app) {
    private val usuarioDAO = AppDatabase.getDatabase(app).usuarioDao()
    val usuarios = usuarioDAO.obtenerUsuarios()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    fun registrar(nombre: String, email: String, password: String, confirmPassword: String, run: String, direccion: String) = viewModelScope.launch {
        if (password == confirmPassword) {
            val user = Usuario(nombre = nombre, email = email, password = password, run = run, direccion = direccion)
            usuarioDAO.insert(user)
            _mensaje.value = "Usuario registrado correctamente"
        } else {
            _mensaje.value = "Las contraseñas no coinciden"
        }
    }

    fun login (username:String, password:String) = viewModelScope.launch {
        val user = usuarioDAO.login(username, password)
        if (user != null) {
            _mensaje.value = "Login exitoso"
        } else {
            _mensaje.value = "Usuario o contraseña incorrectos"
        }
    }
    fun deleteUser(username:String) = viewModelScope.launch {
        usuarioDAO.deleteUser(username)
    }
    fun updateUsuario(username:String, correo:String, password:String) = viewModelScope.launch {
        usuarioDAO.updateUsuario(username, correo, password)
    }
}
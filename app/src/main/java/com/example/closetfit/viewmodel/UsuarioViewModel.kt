package com.example.closetfit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.model.Usuario
import com.example.closetfit.repository.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application) : AndroidViewModel(app) {
    private val usuarioDAO = AppDatabase.getDatabase(app).usuarioDao()
    val usuarios = usuarioDAO.obtenerUsuarios()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

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
        if (username == "adminadmin" && password == "admin123") {
            _currentUser.value = Usuario(nombre = "adminadmin", email = "admin@cfit.com", password = "", run = "0-0", direccion = "Oficina Central")
            _mensaje.value = "Login exitoso"
            return@launch
        }

        val user = usuarioDAO.login(username, password)
        _currentUser.value = user
        if (user != null) {
            _mensaje.value = "Login exitoso"
        } else {
            _mensaje.value = "Usuario o contraseña incorrectos"
        }
    }

    fun logout() {
        _currentUser.value = null
        _mensaje.value = ""
    }

    fun deleteUser(username:String) = viewModelScope.launch {
        usuarioDAO.deleteUser(username)
    }
    fun updateUsuario(username:String, correo:String, password:String) = viewModelScope.launch {
        usuarioDAO.updateUsuario(username, correo, password)
    }
}
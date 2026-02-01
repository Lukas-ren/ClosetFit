package com.example.closetfit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.model.Usuario
import com.example.closetfit.repository.ApiUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ApiUsuarioViewModel(app: Application) : AndroidViewModel(app) {
    private val apiUsuarios = ApiUsuario()

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = apiUsuarios.obtenerUsuarios()
            if (response.isSuccessful) {
                _usuarios.value = response.body() ?: emptyList()
            } else {
                _mensaje.value = "Error al cargar usuarios: ${response.code()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun registrar(nombre: String, email: String, password: String, confirmPassword: String, run: String, direccion: String) = viewModelScope.launch {
        if (password != confirmPassword) {
            _mensaje.value = "Las contraseñas no coinciden"
            return@launch
        }

        _isLoading.value = true
        try {
            val usuario = Usuario(
                id = 0,
                nombre = nombre,
                rol = "USER",
                correo = email,
                contraseña = password,
                run = run,
                direccion = direccion
            )
            val response = apiUsuarios.agregarUsuario(usuario)
            if (response.isSuccessful) {
                _mensaje.value = "Usuario registrado correctamente"
                cargarUsuarios()
            } else {
                _mensaje.value = "Error al registrar: ${response.code()} - ${response.message()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        // Usuario admin hardcodeado
        if (username == "adminadmin" && password == "admin123") {
            _currentUser.value = Usuario(
                id = 0,
                nombre = "adminadmin",
                rol = "ADMIN",
                correo = "admin@cfit.com",
                contraseña = "",
                run = "0-0",
                direccion = "Oficina Central"
            )
            _mensaje.value = "Login exitoso"
            return@launch
        }

        _isLoading.value = true
        try {
            val response = apiUsuarios.obtenerUsuarios()
            if (response.isSuccessful) {
                val usuarios = response.body() ?: emptyList<Usuario>()
                val user = usuarios.find {
                    it.correo == username && it.contraseña == password
                }

                if (user != null) {
                    _currentUser.value = user
                    _mensaje.value = "Login exitoso"
                } else {
                    _currentUser.value = null
                    _mensaje.value = "Usuario o contraseña incorrectos"
                }
            } else {
                _mensaje.value = "Error al iniciar sesión: ${response.code()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun logout() {
        _currentUser.value = null
        _mensaje.value = ""
    }

    fun deleteUser(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val response = apiUsuarios.eliminarUsuario(id)
            if (response.isSuccessful) {
                _mensaje.value = "Usuario eliminado correctamente"
                cargarUsuarios()
            } else {
                _mensaje.value = "Error al eliminar: ${response.code()}"
            }
        } catch (e: Exception) {
            _mensaje.value = "Error de conexión: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = ""
    }
}

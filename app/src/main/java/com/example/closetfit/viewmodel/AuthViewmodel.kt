package com.example.closetfit.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.closetfit.model.FakeDatabase
import com.example.closetfit.model.Usuario

class AuthViewModel : ViewModel() {
    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)
    fun registrar(nombre: String, email: String, password: String, confirmpassword: String, run: String, direccion: String) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || confirmpassword.isBlank() || run.isBlank() || direccion.isBlank()) {
            mensaje.value = "Todos los campos son obligatorios"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mensaje.value = "Correo inválido ❌"
            return
        }
        if (password.length < 4 ) {
            mensaje.value = "La contraseña debe tener mínimo 4 carácteres"
            return
        }
        if(password != confirmpassword){
            mensaje.value = "Las contraseñas no coinciden ❌"
            return
        }
        val nuevo = Usuario(nombre,email,password)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso ✅"
        } else {
            mensaje.value = "El usuario ya existe ❌"
        }
    }

    fun login(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            mensaje.value = "Debes completar todos los campos."
            return false
        }
        val usuario = FakeDatabase.login(email, password)
        return if (usuario != null) {
            usuarioActual.value = usuario.email
            mensaje.value = "Inicio de sesión exitoso 🎉"
            true
        } else {
            mensaje.value = "Credenciales inválidas ❌"
            false
        }
    }
    fun resetLogin() {
        usuarioActual.value = null
        mensaje.value = ""
    }
}
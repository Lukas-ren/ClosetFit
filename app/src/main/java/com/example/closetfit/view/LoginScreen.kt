package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.closetfit.ui.theme.colorBoton
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: UsuarioViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val mensaje by viewModel.mensaje.collectAsState()

    LaunchedEffect(mensaje) {
        if (mensaje == "Login exitoso") {
            if (username == "admin") { // Hardcoded admin credentials
                navController.navigate("usuario_backoffice")
            } else {
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorPrimario,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge, color = Color.White)

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.login(username, password) },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton)
            ) {
                Text("Iniciar sesión")
            }

            Text(mensaje, modifier = Modifier.padding(top = 10.dp), color = Color.White)

            TextButton(onClick = { navController.navigate("registro") }) {
                Text(
                    "¿No tienes cuenta? Crea una aquí",
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}

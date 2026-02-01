package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.closetfit.viewmodel.ApiUsuarioViewModel

@Composable
fun RegistroScreen(navController: NavController, viewModel: ApiUsuarioViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    val mensaje by viewModel.mensaje.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Validaciones locales
    var showErrors by remember { mutableStateOf(false) }

    LaunchedEffect(mensaje) {
        if (mensaje == "Usuario registrado correctamente") {
            navController.navigate("login") {
                popUpTo("registro") { inclusive = true }
            }
            viewModel.limpiarMensaje()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorPrimario,
        contentColor = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Registro",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    enabled = !isLoading,
                    singleLine = true,
                    isError = showErrors && nombre.isBlank(),
                    supportingText = {
                        if (showErrors && nombre.isBlank()) {
                            Text("El nombre es obligatorio", color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    enabled = !isLoading,
                    singleLine = true,
                    isError = showErrors && (email.isBlank() || !email.contains("@")),
                    supportingText = {
                        if (showErrors && email.isBlank()) {
                            Text("El correo es obligatorio", color = Color.Red)
                        } else if (showErrors && !email.contains("@")) {
                            Text("Ingrese un correo válido", color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = run,
                    onValueChange = { run = it },
                    label = { Text("RUN (ej: 12345678-9)") },
                    enabled = !isLoading,
                    singleLine = true,
                    isError = showErrors && run.isBlank(),
                    supportingText = {
                        if (showErrors && run.isBlank()) {
                            Text("El RUN es obligatorio", color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    enabled = !isLoading,
                    singleLine = true,
                    isError = showErrors && direccion.isBlank(),
                    supportingText = {
                        if (showErrors && direccion.isBlank()) {
                            Text("La dirección es obligatoria", color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !isLoading,
                    singleLine = true,
                    isError = showErrors && password.length < 6,
                    supportingText = {
                        if (showErrors && password.isBlank()) {
                            Text("La contraseña es obligatoria", color = Color.Red)
                        } else if (showErrors && password.length < 6) {
                            Text("Mínimo 6 caracteres", color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !isLoading,
                    singleLine = true,
                    isError = showErrors && password != confirmPassword,
                    supportingText = {
                        if (showErrors && password != confirmPassword) {
                            Text("Las contraseñas no coinciden", color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showErrors = true
                        if (nombre.isNotBlank() &&
                            email.isNotBlank() && email.contains("@") &&
                            run.isNotBlank() &&
                            direccion.isNotBlank() &&
                            password.length >= 6 &&
                            password == confirmPassword
                        ) {
                            viewModel.registrar(
                                nombre.trim(),
                                email.trim(),
                                password,
                                confirmPassword,
                                run.trim(),
                                direccion.trim()
                            )
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorBoton,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 8.dp)
                        )
                    }
                    Text(if (isLoading) "Registrando..." else "Registrar")
                }

                if (mensaje.isNotEmpty() && mensaje != "Usuario registrado correctamente") {
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(top = 10.dp),
                        color = if (mensaje.contains("Error") || mensaje.contains("no coinciden")) {
                            Color.Red
                        } else {
                            Color.Green
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = { navController.navigate("login") },
                    enabled = !isLoading
                ) {
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión aquí",
                        color = if (isLoading) Color.Gray else Color.White
                    )
                }
            }
        }
    }
}
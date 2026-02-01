package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.closetfit.model.Usuario
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.ApiUsuarioViewModel

// Stateful Composable
@Composable
fun UsuarioBackoficceScreen(navController: NavController, viewModel: ApiUsuarioViewModel) {
    val usuarios by viewModel.usuarios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }

    UsuarioBackoficceScreenContent(
        navController = navController,
        usuarios = usuarios,
        isLoading = isLoading,
        mensaje = mensaje,
        onRecargar = { viewModel.cargarUsuarios() },
        onEliminarUsuario = { id -> viewModel.deleteUser(id) }
    )
}

// Stateless Composable (for UI and Previews)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBackoficceScreenContent(
    navController: NavController,
    usuarios: List<Usuario>,
    isLoading: Boolean = false,
    mensaje: String = "",
    onRecargar: () -> Unit = {},
    onEliminarUsuario: (Int) -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorPrimario,
        topBar = {
            TopAppBar(
                title = { Text("ClosetFit \nBackoffice de administrador") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorSecundario,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Recargar usuarios") },
                                onClick = {
                                    onRecargar()
                                    menuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Volver al home") },
                                onClick = {
                                    navController.navigate("home")
                                    menuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = {
                                    navController.navigate("login") { popUpTo(0) }
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = colorSecundario, contentColor = Color.Black) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("Usuarios registrados", modifier = Modifier.width(70.dp))
                    Text("Productos registrados", modifier = Modifier.width(70.dp))
                    Text("Ventas registradas", modifier = Modifier.width(70.dp))
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorSecundario
                    )
                }
                mensaje.isNotEmpty() && usuarios.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = mensaje,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = onRecargar) {
                            Text("Reintentar")
                        }
                    }
                }
                usuarios.isEmpty() -> {
                    Text(
                        text = "No hay usuarios registrados",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(usuarios) { usuario ->
                            UserItem(
                                usuario = usuario,
                                onEliminar = { onEliminarUsuario(usuario.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(usuario: Usuario, onEliminar: () -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorSecundario)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${usuario.id}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Badge(
                    containerColor = if (usuario.rol == "ADMIN") Color.Red else Color.Blue
                ) {
                    Text(text = usuario.rol, color = Color.White)
                }
            }
            Text(text = "Nombre: ${usuario.nombre}", color = Color.Black, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Correo: ${usuario.correo}", color = Color.Black)
            Text(text = "Run: ${usuario.run}", color = Color.Black)
            Text(text = "Dirección: ${usuario.direccion}", color = Color.Black)

            if (usuario.rol != "ADMIN") {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar usuario", color = Color.White)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${usuario.nombre}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminar()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioBackoficceScreenPreview() {
    val listaFalsaUser = listOf(
        Usuario(
            id = 1,
            nombre = "Admin",
            rol = "ADMIN",
            correo = "admin@admin.com",
            contraseña = "admin",
            run = "11.111.111-1",
            direccion = "Dirección 123"
        ),
        Usuario(
            id = 2,
            nombre = "TestUser",
            rol = "USER",
            correo = "test@user.com",
            contraseña = "123",
            run = "22.222.222-2",
            direccion = "Avenida 456"
        )
    )
    UsuarioBackoficceScreenContent(
        navController = rememberNavController(),
        usuarios = listaFalsaUser
    )
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    UserItem(
        usuario = Usuario(
            id = 1,
            nombre = "Admin",
            rol = "ADMIN",
            correo = "admin@admin.com",
            contraseña = "admin",
            run = "11.111.111-1",
            direccion = "Dirección 123"
        )
    )
}
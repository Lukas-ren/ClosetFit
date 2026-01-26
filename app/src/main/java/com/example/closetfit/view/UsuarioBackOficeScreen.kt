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
import com.example.closetfit.viewmodel.UsuarioViewModel

// Stateful Composable
@Composable
fun UsuarioBackoficceScreen(navController: NavController, viewModel: UsuarioViewModel) {
    val usuarios by viewModel.usuarios.collectAsState(initial = emptyList())
    UsuarioBackoficceScreenContent(navController = navController, usuarios = usuarios)
}

// Stateless Composable (for UI and Previews)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBackoficceScreenContent(navController: NavController, usuarios: List<Usuario>) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(usuarios) { usuario ->
                UserItem(usuario = usuario)
            }
        }
    }
}

@Composable
fun UserItem(usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorSecundario)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${usuario.nombre}", color = Color.Black)
            Text(text = "Email: ${usuario.email}", color = Color.Black)
            Text(text = "Run: ${usuario.run ?: "No especificado"}", color = Color.Black)
            Text(text = "Dirección: ${usuario.direccion ?: "No especificada"}", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioBackoficceScreenPreview() {
    val fakeUsers = listOf(
        Usuario(nombre = "Admin", email = "admin@admin.com", password = "admin", run = "11.111.111-1", direccion = "Dirección 123"),
        Usuario(nombre = "TestUser", email = "test@user.com", password = "123", run = "22.222.222-2", direccion = "Avenida 456")
    )
    UsuarioBackoficceScreenContent(navController = rememberNavController(), usuarios = fakeUsers)
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    UserItem(usuario = Usuario(nombre = "Admin", email = "admin@admin.com", password = "admin", run = "11.111.111-1", direccion = "Dirección 123"))
}

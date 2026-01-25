package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.closetfit.model.Usuario
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.UsuarioViewModel

// Stateful Composable
@Composable
fun UsuarioBackoficceScreen(viewModel: UsuarioViewModel) {
    val usuarios by viewModel.usuarios.collectAsState(initial = emptyList())
    UsuarioBackoficceScreenContent(usuarios = usuarios)
}

// Stateless Composable (for UI and Previews)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBackoficceScreenContent(usuarios: List<Usuario>) {
    Scaffold(
        containerColor = colorPrimario,
        topBar = {
            TopAppBar(
                title = { Text("ClosetFit \nBackoffice de administrador") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorSecundario
                )
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
    UsuarioBackoficceScreenContent(usuarios = fakeUsers)
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    UserItem(usuario = Usuario(nombre = "Admin", email = "admin@admin.com", password = "admin", run = "11.111.111-1", direccion = "Dirección 123"))
}
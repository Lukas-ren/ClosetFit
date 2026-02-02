package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.closetfit.R
import com.example.closetfit.model.Producto
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.ApiProductoViewModel

// Stateful Composable
@Composable
fun ProductoBackOfficeScreen(
    navController: NavController,
    viewModel: ApiProductoViewModel
) {
    val productos by viewModel.allProductos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    ProductoBackOfficeScreenContent(
        navController = navController,
        productos = productos,
        isLoading = isLoading,
        mensaje = mensaje,
        onRecargar = { viewModel.cargarProductos() },
        onEliminarProducto = { id -> viewModel.eliminarProducto(id) }
    )
}

// Stateless Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoBackOfficeScreenContent(
    navController: NavController,
    productos: List<Producto>,
    isLoading: Boolean = false,
    mensaje: String = "",
    onRecargar: () -> Unit = {},
    onEliminarProducto: (Int) -> Unit = {}
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
                                text = { Text("Recargar productos") },
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
                                    navController.navigate("login") {
                                        popUpTo(0)
                                    }
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = colorSecundario, contentColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("usuario_backoffice") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Usuarios",
                            tint = Color.White
                        )
                    },
                    label = { Text("Usuarios", color = Color.White, fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Ya estás aquí */ },
                    icon = {
                        Icon(
                            Icons.Default.ShoppingBag,
                            contentDescription = "Productos",
                            tint = Color.White
                        )
                    },
                    label = { Text("Productos", color = Color.White, fontSize = 11.sp) }
                )
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
                mensaje.isNotEmpty() && productos.isEmpty() -> {
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
                productos.isEmpty() -> {
                    Text(
                        text = "No hay productos registrados",
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
                        items(productos) { producto ->
                            ProductoItem(
                                producto = producto,
                                onEliminar = { onEliminarProducto(producto.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, onEliminar: () -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorSecundario)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Imagen del producto
                AsyncImage(
                    model = producto.urlImagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_launcher_background),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ID: ${producto.id}",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Badge(
                            containerColor = when (producto.categoria.lowercase()) {
                                "polera" -> Color(0xFF4CAF50)
                                "jeans" -> Color(0xFF2196F3)
                                "short" -> Color(0xFFFF9800)
                                else -> Color.Gray
                            }
                        ) {
                            Text(
                                text = producto.categoria.uppercase(),
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = producto.nombre,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Talla: ${producto.talla}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Precio: $${"%.0f".format(producto.precio)}",
                        color = Color(0xFFD8D78F),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Stock: ${producto.stock}",
                        color = if (producto.stock > 0) Color.Green else Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar producto", color = Color.White)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar ${producto.nombre}?") },
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
fun ProductoBackOfficeScreenPreview() {
    val listaFalsaProductos = listOf(
        Producto(
            id = 1,
            nombre = "Polera Básica Blanca",
            categoria = "polera",
            talla = "M",
            precio = 15990.0,
            urlImagen = "https://example.com/polera.jpg",
            stock = 50,
            descripcion = "Polera básica de algodón 100%"
        ),
        Producto(
            id = 2,
            nombre = "Jeans Skinny Negro",
            categoria = "jeans",
            talla = "32",
            precio = 39990.0,
            urlImagen = "https://example.com/jeans.jpg",
            stock = 35,
            descripcion = "Jeans ajustados de corte moderno"
        ),
        Producto(
            id = 3,
            nombre = "Short Deportivo",
            categoria = "short",
            talla = "L",
            precio = 22990.0,
            urlImagen = "https://example.com/short.jpg",
            stock = 0,
            descripcion = "Short ideal para deporte"
        )
    )
    ProductoBackOfficeScreenContent(
        navController = rememberNavController(),
        productos = listaFalsaProductos
    )
}

@Preview(showBackground = true)
@Composable
fun ProductoItemPreview() {
    ProductoItem(
        producto = Producto(
            id = 1,
            nombre = "Polera Básica Blanca",
            categoria = "polera",
            talla = "M",
            precio = 15990.0,
            urlImagen = "https://example.com/polera.jpg",
            stock = 50,
            descripcion = "Polera básica de algodón 100%"
        )
    )
}
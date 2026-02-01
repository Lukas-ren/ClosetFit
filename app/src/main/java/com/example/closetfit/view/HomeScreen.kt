package com.example.closetfit.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.closetfit.R
import com.example.closetfit.model.Producto
import com.example.closetfit.ui.theme.ClosetFitTheme
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.HomeViewModel
import com.example.closetfit.viewmodel.ApiUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    usuarioViewModel: ApiUsuarioViewModel
) {
    val productos by homeViewModel.productos.collectAsState()
    val isLoading by homeViewModel.loading.collectAsState()
    val mensaje by homeViewModel.mensaje.collectAsState()
    val currentUser by usuarioViewModel.currentUser.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }

    // Filtrar productos basándose en búsqueda y categoría
    val productosFiltrados = remember(productos, searchQuery, selectedCategory) {
        productos.filter { producto ->
            val matchesSearch = searchQuery.isBlank() ||
                    producto.nombre.contains(searchQuery, ignoreCase = true) ||
                    producto.descripcion.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == "Todos" ||
                    producto.categoria.equals(selectedCategory, ignoreCase = true)

            matchesSearch && matchesCategory
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.cargarProductos()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = colorPrimario
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    TopSection(
                        navController = navController,
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        userName = currentUser?.nombre ?: "Usuario"
                    )
                }

                item {
                    CategoriesSection(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                when {
                    isLoading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = colorSecundario)
                            }
                        }
                    }
                    mensaje.isNotEmpty() -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = mensaje,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { homeViewModel.cargarProductos() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                    productosFiltrados.isEmpty() -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isNotBlank() || selectedCategory != "Todos")
                                        "No se encontraron productos"
                                    else
                                        "No hay productos disponibles",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    else -> {
                        items(productosFiltrados) { producto ->
                            ProductoCard(
                                producto = producto,
                                onClick = {
                                    // TODO: Navegar a detalle del producto
                                    // navController.navigate("producto_detalle/${producto.id}")
                                }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSection(
    navController: NavController,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    userName: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Hola, $userName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(onClick = { navController.navigate("perfil") }) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Buscar productos...", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Gray)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}

@Composable
fun CategoriesSection(
    selectedCategory: String = "Todos",
    onCategorySelected: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorSecundario)
            .padding(16.dp)
    ) {
        Text(
            text = "Categorías",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CategoryItem(
                imageRes = R.drawable.polera_negra,
                text = "Poleras",
                isSelected = selectedCategory == "polera",
                onClick = { onCategorySelected("polera") }
            )
            CategoryItem(
                imageRes = R.drawable.pantalon_jean,
                text = "Jeans",
                isSelected = selectedCategory == "jeans",
                onClick = { onCategorySelected("jeans") }
            )
            CategoryItem(
                imageRes = R.drawable.shorts_deportivos,
                text = "Shorts",
                isSelected = selectedCategory == "short",
                onClick = { onCategorySelected("short") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedCategory != "Todos") {
            TextButton(
                onClick = { onCategorySelected("Todos") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Ver todos", color = Color.White)
            }
        }
    }
}

@Composable
fun CategoryItem(
    @DrawableRes imageRes: Int,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFD8D78F) else Color.White)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = text,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) Color(0xFFD8D78F) else Color.White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorSecundario,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Usar AsyncImage de Coil para cargar imágenes desde URL
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

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = producto.categoria.capitalize(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Talla: ${producto.talla}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "$${"%.0f".format(producto.precio)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD8D78F)
                        )
                    }

                    Text(
                        text = "Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (producto.stock > 0) Color.Green else Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = colorSecundario,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
            label = { Text("Inicio", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                // TODO: Implementar carrito
                // navController.navigate("carrito")
            },
            icon = {
                BadgedBox(
                    badge = { Badge { Text("0") } }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White)
                }
            },
            label = { Text("Carrito", color = Color.White) }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeScreenPreview() {
    ClosetFitTheme {
        HomeScreenContent(
            productos = listOf(
                Producto(
                    id = 1,
                    nombre = "Polera Básica Blanca",
                    categoria = "polera",
                    talla = "M",
                    precio = 15990.0,
                    urlImagen = "https://example.com/polera.jpg",
                    stock = 50,
                    descripcion = "Polera básica de algodón 100%"
                )
            ),
            isLoading = false,
            mensaje = "",
            userName = "Usuario"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    productos: List<Producto>,
    isLoading: Boolean,
    mensaje: String,
    userName: String
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = colorPrimario
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                TopSection(
                    navController = navController,
                    searchQuery = "",
                    onSearchChange = {},
                    userName = userName
                )
            }
            item { CategoriesSection() }
            items(productos) { producto ->
                ProductoCard(producto = producto, onClick = {})
            }
        }
    }
}
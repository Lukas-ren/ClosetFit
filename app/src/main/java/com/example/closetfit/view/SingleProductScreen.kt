package com.example.closetfit.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.closetfit.viewmodel.CarritoViewModel
import com.example.closetfit.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleProductScreen(
    navController: NavController,
    productoId: Int,
    homeViewModel: HomeViewModel,
    carritoViewModel: CarritoViewModel
) {
    val productos by homeViewModel.productos.collectAsState()
    val producto = productos.find { it.id == productoId }

    if (producto != null) {
        SingleProductContent(
            navController = navController,
            producto = producto,
            onAddToCart = { cantidad ->
                carritoViewModel.agregarProducto(producto, cantidad)
            }
        )
    } else {
        // Producto no encontrado
        Scaffold(
            containerColor = colorPrimario,
            topBar = {
                TopAppBar(
                    title = { Text("Producto") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorSecundario,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Producto no encontrado",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleProductContent(
    navController: NavController,
    producto: Producto,
    onAddToCart: (Int) -> Unit = {}
) {
    var cantidad by remember { mutableStateOf(1) }
    var showAddedDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorPrimario,
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorSecundario,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorSecundario,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Precio total",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            "$${"%.0f".format(producto.precio * cantidad)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD8D78F)
                        )
                    }

                    Button(
                        onClick = {
                            if (producto.stock > 0) {
                                onAddToCart(cantidad)
                                showAddedDialog = true
                            }
                        },
                        enabled = producto.stock > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD8D78F),
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray
                        ),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (producto.stock > 0) "Agregar al carrito" else "Sin stock",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = producto.urlImagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_launcher_background),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                )

                // Badge de categoría
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(20.dp),
                    color = when (producto.categoria.lowercase()) {
                        "polera" -> Color(0xFF4CAF50)
                        "jeans" -> Color(0xFF2196F3)
                        "short" -> Color(0xFFFF9800)
                        else -> Color.Gray
                    }
                ) {
                    Text(
                        text = producto.categoria.uppercase(),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Indicador de stock
                if (producto.stock <= 5) {
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart),
                        shape = RoundedCornerShape(20.dp),
                        color = if (producto.stock == 0) Color.Red else Color(0xFFFF9800)
                    ) {
                        Text(
                            text = if (producto.stock == 0) "SIN STOCK" else "¡ÚLTIMAS UNIDADES!",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Información del producto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Nombre
                Text(
                    text = producto.nombre,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Precio unitario
                Text(
                    text = "$${"%.0f".format(producto.precio)} c/u",
                    fontSize = 20.sp,
                    color = Color(0xFFD8D78F),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Detalles en cards
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorSecundario
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DetailRow("Talla", producto.talla)
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailRow("Stock disponible", "${producto.stock} unidades")
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailRow("Categoría", producto.categoria.capitalize())
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Descripción
                Text(
                    text = "Descripción",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorSecundario
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = producto.descripcion,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 16.sp,
                        color = Color.White,
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Selector de cantidad
                if (producto.stock > 0) {
                    Text(
                        text = "Cantidad",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = colorSecundario
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (cantidad > 1) cantidad-- },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color(0xFFD8D78F)
                                ),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.Remove,
                                    contentDescription = "Reducir",
                                    tint = Color.Black
                                )
                            }

                            Text(
                                text = cantidad.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )

                            IconButton(
                                onClick = { if (cantidad < producto.stock) cantidad++ },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color(0xFFD8D78F)
                                ),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Aumentar",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    // Diálogo de confirmación
    if (showAddedDialog) {
        AlertDialog(
            onDismissRequest = { showAddedDialog = false },
            icon = {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFFD8D78F),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("¡Producto agregado!") },
            text = {
                Text(
                    "Se agregaron $cantidad ${if (cantidad == 1) "unidad" else "unidades"} de ${producto.nombre} al carrito.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAddedDialog = false
                        navController.navigate("carrito")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD8D78F),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Ir al carrito")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddedDialog = false }) {
                    Text("Seguir comprando")
                }
            }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SingleProductScreenPreview() {
    ClosetFitTheme {
        SingleProductContent(
            navController = rememberNavController(),
            producto = Producto(
                id = 1,
                nombre = "Polera Básica Blanca Premium",
                categoria = "polera",
                talla = "M",
                precio = 15990.0,
                urlImagen = "https://example.com/polera.jpg",
                stock = 50,
                descripcion = "Polera básica de algodón 100% con corte moderno. Ideal para uso diario, combina con todo tu guardarropa. Material suave y transpirable."
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SingleProductScreenSinStockPreview() {
    ClosetFitTheme {
        SingleProductContent(
            navController = rememberNavController(),
            producto = Producto(
                id = 2,
                nombre = "Jeans Skinny Negro",
                categoria = "jeans",
                talla = "32",
                precio = 39990.0,
                urlImagen = "https://example.com/jeans.jpg",
                stock = 0,
                descripcion = "Jeans ajustados de corte moderno con elasticidad para mayor comodidad."
            )
        )
    }
}
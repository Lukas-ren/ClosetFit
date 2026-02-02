package com.example.closetfit.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.closetfit.R
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel
) {
    val carrito by carritoViewModel.carrito.collectAsState()
    val totalCompra by carritoViewModel.total.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorPrimario,
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (carrito.isNotEmpty()) {
                        TextButton(onClick = { showConfirmDialog = true }) {
                            Text("Vaciar", color = Color.Red)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorSecundario,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (carrito.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = colorSecundario,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Subtotal:",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                "$${"%.0f".format(totalCompra)}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total:",
                                color = Color(0xFFD8D78F),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "$${"%.0f".format(totalCompra)}",
                                color = Color(0xFFD8D78F),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // TODO: Implementar proceso de compra
                                // navController.navigate("cargando_compra")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD8D78F),
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Finalizar compra",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (carrito.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Tu carrito está vacío",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Agrega productos para comenzar tu compra",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("home") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD8D78F),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Ir a comprar")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(carrito) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = colorSecundario
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagen del producto
                                AsyncImage(
                                    model = item.urlImagen,
                                    contentDescription = item.nombre,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(id = R.drawable.ic_launcher_background),
                                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.nombre,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 2
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Talla: ${item.talla}",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        text = "$${"%.0f".format(item.precio)} c/u",
                                        color = Color(0xFFD8D78F),
                                        fontSize = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Controles de cantidad
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = { carritoViewModel.disminuirCantidad(item.id) },
                                            modifier = Modifier.size(32.dp),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = Color.White.copy(alpha = 0.2f)
                                            )
                                        ) {
                                            Icon(
                                                Icons.Default.Remove,
                                                contentDescription = "Disminuir",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        Text(
                                            text = "${item.cantidad}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.width(30.dp),
                                            textAlign = TextAlign.Center
                                        )

                                        IconButton(
                                            onClick = { carritoViewModel.aumentarCantidad(item.id) },
                                            modifier = Modifier.size(32.dp),
                                            enabled = item.cantidad < item.stockDisponible,
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = Color.White.copy(alpha = 0.2f),
                                                disabledContainerColor = Color.Gray.copy(alpha = 0.2f)
                                            )
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Aumentar",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.weight(1f))

                                        // Subtotal
                                        Text(
                                            text = "$${"%.0f".format(item.subtotal)}",
                                            color = Color(0xFFD8D78F),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )

                                        IconButton(
                                            onClick = { carritoViewModel.quitarProducto(item.id) },
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = Color.Red.copy(alpha = 0.2f)
                                            )
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para vaciar carrito
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Vaciar carrito") },
            text = { Text("¿Estás seguro de que deseas eliminar todos los productos del carrito?") },
            confirmButton = {
                Button(
                    onClick = {
                        carritoViewModel.vaciarCarrito()
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Vaciar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
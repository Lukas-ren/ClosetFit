package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.closetfit.model.DetallePedido
import com.example.closetfit.ui.theme.colorBoton
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.CarritoViewmodel
import com.example.closetfit.viewmodel.CompraExitosaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewmodel: CarritoViewmodel = viewModel(),
    compraExitosaViewModel: CompraExitosaViewModel = viewModel()
) {
    val carrito = carritoViewmodel.carrito.collectAsState()
    val totalCompra by carritoViewmodel.total.collectAsState()

    Scaffold(
        containerColor = colorPrimario,
        contentColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("🛒 Carrito de Compras") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorSecundario,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorSecundario,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Total: $${totalCompra}",
                        color = Color.White
                    )

                    Button(
                        onClick = {
                            navController.navigate("cargando_compra")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorBoton
                        )
                    ) {
                        Text("Finalizar compra")
                    }

                }
            }
        }

    ) { padding ->
        if (carrito.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío 🛍️")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(carrito.value) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3A3A3A)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // imagenes de los productos en el carrito

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Precio: $${producto.precio}")
                                Text("Subtotal: $${producto.subtotal}")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(onClick = { carritoViewmodel.disminuirCantidad(producto.id) }) {
                                    Text("-")
                                }
                                Text("${producto.cantidad}", modifier = Modifier.padding(horizontal = 4.dp))
                                Button(
                                    onClick = {
                                        carritoViewmodel.agregarProducto(
                                            com.example.closetfit.model.Carrito(
                                                id = producto.id,
                                                nombre = producto.nombre,
                                                precio = producto.precio,
                                                imagen = producto.imagen,
                                                cantidad = producto.cantidad
                                            )
                                        )
                                    }
                                ) {
                                    Text(text = "+")
                                }
                                IconButton(onClick = { carritoViewmodel.quitarProducto(producto.id) }) {
                                    Text("🗑️")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

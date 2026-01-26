package com.example.closetfit.view

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.closetfit.ui.theme.colorBoton
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.CompraExitosaViewModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Defaults


@Composable
fun CompraExitosaScreen(navController: NavController, viewModel: CompraExitosaViewModel) {
    val detalles by viewModel.detallesPedido.collectAsState()
    val navegarInicio by viewModel.navegarInicio.observeAsState(false)
    val navegarTienda by viewModel.navegarTienda.observeAsState(false)

    LaunchedEffect(Unit) {
        println("DETALLES RECIBIDOS → $detalles")
        println("CANTIDAD = ${detalles?.numeroArticulos}")
        Log.e("COMPRA_EXITOSA", "🧾 Artículos recibidos = ${detalles?.numeroArticulos}")

        val cantidad = detalles?.numeroArticulos ?: 0
    }

    LaunchedEffect(key1 = navegarInicio, key2 = navegarTienda) {
        when {
            navegarInicio -> {
                viewModel.navegacionCompletada()
                navController.navigate("inicio") {
                    popUpTo("carrito") { inclusive = true }
                }
            }

            navegarTienda -> {
                viewModel.navegacionCompletada()
                navController.navigate("catalogo") {
                    popUpTo("carrito") { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        containerColor = colorSecundario,
        contentColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFFD8D78F),
                    modifier = Modifier
                        .size(96.dp)
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )
            }
            Text(
                "¡Compra realizada con éxito!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            detalles?.let {
                Text("Número de pedido: ${it.idPedido}")
                Text("Artículos: ${it.numeroArticulos}")
                Text("Método de pago: ${it.metodoPago}")
                Text("Total: $${it.totalCompra}")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { viewModel.navegarAInicio() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorBoton)
                ) {
                    Text("Salir de la aplicación")
                }

                Button(
                    onClick = { viewModel.navegarATienda() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorBoton)
                ) {
                    Text("Seguir comprando")
                }
            }
        }
    }
}
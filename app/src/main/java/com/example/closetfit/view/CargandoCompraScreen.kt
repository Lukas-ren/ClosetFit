package com.example.closetfit.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.viewmodel.CargandoCompraViewModel
import com.example.closetfit.viewmodel.CompraExitosaViewModel
import com.example.closetfit.viewmodel.CompraRechazadaViewModel
import com.example.closetfit.model.ResultadoCompra
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.CarritoViewmodel

@Composable
fun CargandoCompraScreen(
    navController: NavController,
    viewModel: CargandoCompraViewModel = viewModel(),
    carritoViewmodel: CarritoViewmodel,
    compraExitosaViewModel: CompraExitosaViewModel = viewModel(),
    compraRechazadaViewModel: CompraRechazadaViewModel = viewModel()
) {
    val estado by viewModel.estadoCompra.collectAsState()

    val carritoVacio = carritoViewmodel.carrito.value.isEmpty()

    LaunchedEffect(Unit) {
        viewModel.procesarCompra(carritoVacio)
    }

    LaunchedEffect(estado) {
        when (estado) {
            is ResultadoCompra.Exitosa -> {
                navController.navigate("compra_exitosa") {
                    popUpTo("cargandoCompra") { inclusive = true }
                }
                viewModel.limpiarEstado()
            }

            is ResultadoCompra.Rechazada -> {
                val motivo = (estado as ResultadoCompra.Rechazada).motivo
                compraRechazadaViewModel.setMotivo(motivo)

                navController.navigate("compra_rechazada") {
                    popUpTo("cargandoCompra") { inclusive = true }
                }
                viewModel.limpiarEstado()
            }

            null -> Unit
        }
    }

    Scaffold(
        containerColor = colorPrimario,
        contentColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = colorSecundario,
                strokeWidth = 4.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Procesando tu compra...")
        }
    }
}


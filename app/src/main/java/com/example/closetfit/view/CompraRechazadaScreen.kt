package com.example.closetfit.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.closetfit.ui.theme.colorBoton
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.CompraRechazadaViewModel
import kotlin.text.ifEmpty

@Composable
fun CompraRechazadaScreen(
    navController: NavController,
    viewModel: CompraRechazadaViewModel = viewModel()
) {
    val motivo by viewModel.motivoRechazo.observeAsState("")

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

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color(0xFFD8D78F),
                    modifier = Modifier
                        .size(96.dp)
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Compra rechazada.",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = motivo.ifEmpty {
                        "Hubo un problema con el pago o los datos ingresados."
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("carrito") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text("Volver al carrito")
            }
        }
    }
}

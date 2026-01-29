package com.example.closetfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.closetfit.ui.theme.ClosetFitTheme
import com.example.closetfit.view.CargandoCompraScreen
import com.example.closetfit.view.CarritoScreen
import com.example.closetfit.view.CompraExitosaScreen
import com.example.closetfit.view.CompraRechazadaScreen
import com.example.closetfit.view.HomeScreen
import com.example.closetfit.view.LoginScreen
import com.example.closetfit.view.PerfilScreen
import com.example.closetfit.view.RegistroScreen
import com.example.closetfit.view.UsuarioBackoficceScreen
import com.example.closetfit.viewmodel.CargandoCompraViewModel
import com.example.closetfit.viewmodel.CarritoViewmodel
import com.example.closetfit.viewmodel.UsuarioViewModel
import com.example.closetfit.viewmodel.CompraExitosaViewModel
import com.example.closetfit.viewmodel.CompraRechazadaViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClosetFitTheme {
                val navController = rememberNavController()
                val usuarioViewModel: UsuarioViewModel = viewModel()
                val carritoViewmodel: CarritoViewmodel = viewModel()
                val compraExitosaViewModel: CompraExitosaViewModel = viewModel()
                val compraRechazadaViewModel: CompraRechazadaViewModel = viewModel()
                val cargandoCompraViewmodel: CargandoCompraViewModel = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = "home" 
                ) {
                    composable("home") { 
                        HomeScreen(navController = navController)
                    }
                    composable("carrito") { 
                        CarritoScreen(navController = navController,
                            carritoViewmodel = carritoViewmodel)

                    }
                    composable("perfil") { 
                        PerfilScreen(navController = navController, viewModel = usuarioViewModel)
                    }
                    composable("registro") {
                        RegistroScreen(
                            navController = navController,
                            viewModel = usuarioViewModel
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            viewModel = usuarioViewModel
                        )
                    }
                    composable("cargando_compra") {
                        CargandoCompraScreen(
                            navController = navController,
                            viewModel = cargandoCompraViewmodel,
                            carritoViewmodel = carritoViewmodel,
                            compraExitosaViewModel = compraExitosaViewModel,
                            compraRechazadaViewModel = compraRechazadaViewModel
                        )
                    }


                    composable("compra_exitosa") {
                        CompraExitosaScreen(
                            navController = navController,
                            viewModel = compraExitosaViewModel
                        )
                    }
                    composable("compra_rechazada") {
                        CompraRechazadaScreen(
                            navController = navController,
                            viewModel = compraRechazadaViewModel
                        )
                    }

                    composable("usuario_backoffice") {
                        UsuarioBackoficceScreen(navController = navController, viewModel = usuarioViewModel)
                    }

                }
            }
        }
    }
}

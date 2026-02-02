package com.example.closetfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.closetfit.ui.theme.ClosetFitTheme
import com.example.closetfit.view.CargandoCompraScreen
import com.example.closetfit.view.CarritoScreen
import com.example.closetfit.view.CompraExitosaScreen
import com.example.closetfit.view.CompraRechazadaScreen
import com.example.closetfit.view.HomeScreen
import com.example.closetfit.view.LoginScreen
import com.example.closetfit.view.PerfilScreen
import com.example.closetfit.view.ProductoBackOfficeScreen
import com.example.closetfit.view.RegistroScreen
import com.example.closetfit.view.SingleProductScreen
import com.example.closetfit.view.UsuarioBackoficceScreen
import com.example.closetfit.viewmodel.ApiProductoViewModel
import com.example.closetfit.viewmodel.CargandoCompraViewModel
import com.example.closetfit.viewmodel.CarritoViewModel
import com.example.closetfit.viewmodel.CompraExitosaViewModel
import com.example.closetfit.viewmodel.CompraRechazadaViewModel
import com.example.closetfit.viewmodel.HomeViewModel
import com.example.closetfit.viewmodel.ApiUsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClosetFitTheme {
                val navController = rememberNavController()
                val productoViewModel: ApiProductoViewModel = viewModel()
                val usuarioViewModel: ApiUsuarioViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()

                val carritoViewModel: CarritoViewModel = viewModel()
                val compraExitosaViewModel: CompraExitosaViewModel = viewModel()
                val compraRechazadaViewModel: CompraRechazadaViewModel = viewModel()
                val cargandoCompraViewModel: CargandoCompraViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "auth"
                ) {
                    // Auth Graph
                    navigation(startDestination = "login", route = "auth") {
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                viewModel = usuarioViewModel
                            )
                        }

                        composable("registro") {
                            RegistroScreen(
                                navController = navController,
                                viewModel = usuarioViewModel
                            )
                        }
                    }

                    // Main Graph (for USER role)
                    navigation(startDestination = "home", route = "main") {
                        composable("home") {
                            HomeScreen(
                                navController = navController,
                                homeViewModel = homeViewModel,
                                apiUsuarioViewModel = usuarioViewModel
                            )
                        }

                        composable("perfil") {
                            PerfilScreen(
                                navController = navController,
                                viewModel = usuarioViewModel
                            )
                        }

                        composable("carrito") {
                            CarritoScreen(
                                navController = navController,
                                carritoViewModel = carritoViewModel
                            )
                        }

                        composable("cargando_compra") {
                            CargandoCompraScreen(
                                navController = navController,
                                viewModel = cargandoCompraViewModel,
                                carritoViewModel = carritoViewModel,
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

                        composable("producto_detalle/{productoId}") { backStackEntry ->
                            val productoId = backStackEntry.arguments?.getString("productoId")?.toIntOrNull() ?: 0
                            SingleProductScreen(
                                navController = navController,
                                productoId = productoId,
                                homeViewModel = homeViewModel,
                                carritoViewModel = carritoViewModel
                            )
                        }
                    }



                    // Admin Backoffice Graph
                    navigation(startDestination = "usuario_backoffice", route = "admin") {
                        composable("usuario_backoffice") {
                            UsuarioBackoficceScreen(
                                navController = navController,
                                viewModel = usuarioViewModel
                            )
                        }

                        composable("producto_backoffice"){
                            ProductoBackOfficeScreen(
                                navController = navController,
                                viewModel = productoViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

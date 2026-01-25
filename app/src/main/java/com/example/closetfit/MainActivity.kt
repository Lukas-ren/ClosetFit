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
import com.example.closetfit.view.LoginScreen
import com.example.closetfit.view.RegistroScreen
import com.example.closetfit.view.UsuarioBackoficceScreen
import com.example.closetfit.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClosetFitTheme {
                val navController = rememberNavController()
                val usuarioViewModel: UsuarioViewModel = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = "registro"
                ) {
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
                    composable("usuario_backoffice") {
                        UsuarioBackoficceScreen(viewModel = usuarioViewModel)
                    }
                }
            }
        }
    }
}

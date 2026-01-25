package com.example.closetfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.closetfit.ui.theme.ClosetFitTheme
import com.example.closetfit.view.LoginScreen
import com.example.closetfit.viewmodel.AuthViewModel
import com.example.closetfit.view.RegistroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClosetFitTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = "registro"
                ) {
                    composable("registro") {
                        RegistroScreen(
                            navController = navController,
                            viewModel = authViewModel
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            viewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}

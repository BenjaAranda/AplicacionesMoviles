// ruta: navigation/AppNavigation.kt
package com.example.app_prueba.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_prueba.ui.screens.home.HomeScreen
import com.example.app_prueba.ui.screens.login.LoginScreen
import com.example.app_prueba.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Routes.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}
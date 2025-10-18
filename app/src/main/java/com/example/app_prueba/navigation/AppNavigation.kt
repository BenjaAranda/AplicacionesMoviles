package com.example.app_prueba.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_prueba.ui.screens.cart.CartScreen
import com.example.app_prueba.ui.screens.detail.ProductDetailScreen
import com.example.app_prueba.ui.screens.home.HomeScreen
import com.example.app_prueba.ui.screens.login.LoginScreen
import com.example.app_prueba.ui.screens.profile.ProfileScreen
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
        composable(
            route = Routes.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            requireNotNull(productId)
            ProductDetailScreen(productId = productId, navController = navController)
        }
        composable(Routes.Cart.route) {
            CartScreen(navController = navController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
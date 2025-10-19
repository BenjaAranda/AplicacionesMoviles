package com.example.app_prueba.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Lista de pantallas que tendrán la barra de navegación inferior
    val bottomBarScreens = listOf(
        Routes.Home.route,
        Routes.Cart.route,
        Routes.Profile.route
    )

    // Determina si la pantalla actual debe mostrar la barra de navegación
    val showBottomBar = currentDestination?.route in bottomBarScreens

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Cart,
                    BottomNavItem.Profile
                )
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Login.route,
            modifier = Modifier.padding(innerPadding) // Aplica el padding del Scaffold
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
            composable(Routes.Cart.route) {
                CartScreen(navController = navController)
            }
            composable(Routes.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(
                route = Routes.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                requireNotNull(productId)
                ProductDetailScreen(productId = productId, navController = navController)
            }
        }
    }
}
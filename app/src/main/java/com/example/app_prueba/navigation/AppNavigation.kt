package com.example.app_prueba.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
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
import com.example.app_prueba.ui.components.TopBar
import com.example.app_prueba.ui.screens.about.AboutUsScreen
import com.example.app_prueba.ui.screens.account.AccountScreen
import com.example.app_prueba.ui.screens.blog.BlogScreen
import com.example.app_prueba.ui.screens.cart.CartScreen
import com.example.app_prueba.ui.screens.contact.ContactScreen
import com.example.app_prueba.ui.screens.detail.ProductDetailScreen
import com.example.app_prueba.ui.screens.home.HomeScreen
import com.example.app_prueba.ui.screens.login.LoginScreen
import com.example.app_prueba.ui.screens.products.ProductsScreen
import com.example.app_prueba.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Lista de rutas que deben mostrarse a pantalla completa (sin TopBar ni BottomBar)
    val fullScreenRoutes = listOf(Routes.Login.route, Routes.Register.route)
    val shouldShowBars = currentDestination?.route !in fullScreenRoutes

    Scaffold(
        // Mostramos la TopBar solo si la ruta actual no es de pantalla completa
        topBar = {
            if (shouldShowBars) {
                TopBar(navController = navController)
            }
        },
        // Mostramos la BottomBar solo si la ruta actual no es de pantalla completa
        bottomBar = {
            if (shouldShowBars) {
                NavigationBar {
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Cart,
                        BottomNavItem.Account
                    )
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
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Rutas que se mostrarán SIN barras de navegación
            composable(Routes.Login.route) { LoginScreen(navController = navController) }
            composable(Routes.Register.route) { RegisterScreen(navController = navController) }

            // Rutas que se mostrarán CON las barras de navegación
            composable(Routes.Home.route) { HomeScreen(navController = navController) }
            composable(Routes.Products.route) { ProductsScreen(navController = navController) }
            composable(Routes.AboutUs.route) { AboutUsScreen() }
            composable(Routes.Blog.route) { BlogScreen() }
            composable(Routes.Contact.route) { ContactScreen() }
            composable(Routes.Cart.route) { CartScreen(navController = navController) }
            composable(Routes.Account.route) { AccountScreen(navController = navController) }
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

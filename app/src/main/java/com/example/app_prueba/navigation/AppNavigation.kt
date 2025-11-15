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
import com.example.app_prueba.ui.screens.puntos.PuntosScreen
import com.example.app_prueba.ui.theme.AppPruebaTheme
import androidx.compose.material3.MaterialTheme

@Composable
fun AppNavigation() {

    AppPruebaTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            topBar = {
                TopBar(navController = navController)
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
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
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary, // Verde para el ícono seleccionado
                                selectedTextColor = MaterialTheme.colorScheme.primary, // Verde para el texto seleccionado
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Morado más tenue
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Morado más tenue
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) // Un sutil fondo verde para el ítem activo
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Todas las rutas de la aplicación
                composable(Routes.Login.route) { LoginScreen(navController = navController) }
                composable(Routes.Register.route) { RegisterScreen(navController = navController) }
                composable(Routes.Home.route) { HomeScreen(navController = navController) }
                composable(Routes.Products.route) { ProductsScreen(navController = navController) }
                composable(Routes.AboutUs.route) { AboutUsScreen(navController = navController) }
                composable(Routes.Blog.route) { BlogScreen(navController = navController) }
                composable(Routes.Contact.route) { ContactScreen(navController = navController) }
                composable(Routes.Cart.route) { CartScreen(navController = navController) }
                composable(Routes.Account.route) { AccountScreen(navController = navController) }

                // Ruta de Puntos (CORREGIDA)
                composable(route = Routes.Points.route) { PuntosScreen(navController = navController) }

                // Ruta de Detalles del Producto
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
}

package com.example.app_prueba.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Routes.Home.route, // Asegura que la ruta es la de Home
        title = "Inicio",
        icon = Icons.Default.Home
    )
    object Cart : BottomNavItem(
        route = Routes.Cart.route,
        title = "Carrito",
        icon = Icons.Default.ShoppingCart
    )
    object Account : BottomNavItem(
        route = Routes.Account.route,
        title = "Cuenta",
        icon = Icons.Default.Person
    )
}
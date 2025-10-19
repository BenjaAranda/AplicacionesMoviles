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
        route = Routes.Home.route,
        title = "Inicio",
        icon = Icons.Default.Home
    )
    object Cart : BottomNavItem(
        route = Routes.Cart.route,
        title = "Carrito",
        icon = Icons.Default.ShoppingCart
    )
    object Profile : BottomNavItem(
        route = Routes.Profile.route,
        title = "Perfil",
        icon = Icons.Default.Person
    )
}
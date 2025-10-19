package com.example.app_prueba.navigation

sealed class Routes(val route: String) {
    // Rutas de Autenticación
    object Login : Routes("login")
    object Register : Routes("register")

    // Rutas Principales (Top Bar)
    object Home : Routes("home")
    object Products : Routes("products")
    object AboutUs : Routes("about_us")
    object Blog : Routes("blog")
    object Contact : Routes("contact")

    // Rutas Secundarias
    object ProductDetail : Routes("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Cart : Routes("cart")
    object Profile : Routes("profile")
}
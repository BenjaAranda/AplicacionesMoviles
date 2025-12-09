package com.example.app_prueba.navigation

sealed class Routes(val route: String) {
    // ... (Tus rutas anteriores: Login, Register, Home, etc.) ...
    object Login : Routes("login")
    object Register : Routes("register")
    object Home : Routes("home")
    object Products : Routes("products")
    object AboutUs : Routes("about_us")
    object Blog : Routes("blog")
    object Contact : Routes("contact")
    object ProductDetail : Routes("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Cart : Routes("cart")
    object Account : Routes("account")
    object Points : Routes("puntos")

    object Checkout : Routes("checkout")
}

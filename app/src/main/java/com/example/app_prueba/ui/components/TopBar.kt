package com.example.app_prueba.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes

@Composable
fun TopBar(navController: NavController) {
    val navItems = listOf(
        "Inicio" to Routes.Home.route,
        "Productos" to Routes.Products.route,
        "Nosotros" to Routes.AboutUs.route,
        "Blog" to Routes.Blog.route,
        "Contacto" to Routes.Contact.route
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        navItems.forEach { (title, route) ->
            TextButton(onClick = { navController.navigate(route) }) {
                Text(text = title, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
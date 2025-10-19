package com.example.app_prueba.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.R

// Data class para definir cada elemento de la barra de navegación.
data class NavItem(val label: String, val route: String)

@Composable
fun TopBar(navController: NavController) {
    // Lista de los elementos de navegación que queremos mostrar.
    // He corregido la lista según tu petición ("Inicio, Productos, Nosotros y Blog").
    val navItems = listOf(
        NavItem("Inicio", Routes.Home.route),
        NavItem("Productos", Routes.Products.route),
        NavItem("Nosotros", Routes.AboutUs.route),
        NavItem("Blog", Routes.Blog.route),
        NavItem("Contacto", Routes.Contact.route)
    )

    // Surface actúa como el contenedor principal de nuestra barra.
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp), // Altura estándar para una TopBar.
        color = MaterialTheme.colorScheme.surface, // Color de fondo de la barra.
        shadowElevation = 4.dp // Pequeña sombra para darle profundidad.
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ---- 1. LOGO (Izquierda) ----
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clickable { navController.navigate(Routes.Home.route) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_main), // CONFIRMA que tu archivo se llama 'logo'
                    contentDescription = "Logo de la Tienda",
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Espacio entre el logo y las opciones de navegación
            Spacer(modifier = Modifier.width(16.dp))

            // ---- 2. OPCIONES DE NAVEGACIÓN (Centro con Scroll) ----
            Row(
                // Ocupa el espacio restante y permite el scroll horizontal.
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                // Añade espacio entre los elementos y los centra verticalmente.
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Obtenemos la ruta actual para saber qué opción resaltar.
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                    Text(
                        text = item.label,
                        fontSize = 16.sp,
                        // El color cambia si la opción está seleccionada.
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        // El modificador no necesita padding, Arrangement.spacedBy se encarga.
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { navController.navigate(item.route) }
                            .padding(vertical = 8.dp) // Mantenemos un padding vertical para el área táctil.
                    )
                }
            }
        }
    }
}

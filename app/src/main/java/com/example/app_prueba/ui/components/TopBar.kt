package com.example.app_prueba.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app_prueba.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para controlar si el menú desplegable está abierto o cerrado.
    var menuExpanded by remember { mutableStateOf(false) }

    val routesWithoutBack = listOf(
        Routes.Home.route,
        Routes.Login.route,
        Routes.Register.route
    )

    TopAppBar(
        title = { Text("Mi Tienda") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer, // Color para los iconos de acción
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Color para el icono de navegación
        ),
        navigationIcon = {
            if (currentRoute !in routesWithoutBack) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        // ----> INICIO DE LA MODIFICACIÓN: MENÚ DE ACCIONES <----
        actions = {
            // Icono de tres puntos que abrirá el menú.
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menú de opciones"
                )
            }

            // Menú desplegable que se muestra cuando menuExpanded es true.
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false } // Se cierra si el usuario toca fuera.
            ) {
                // Cada DropdownMenuItem es una opción en el menú.
                DropdownMenuItem(
                    text = { Text("Sobre Nosotros") },
                    onClick = {
                        navController.navigate(Routes.AboutUs.route)
                        menuExpanded = false // Cierra el menú después de la navegación.
                    }
                )
                DropdownMenuItem(
                    text = { Text("Blog") },
                    onClick = {
                        navController.navigate(Routes.Blog.route)
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Contacto") },
                    onClick = {
                        navController.navigate(Routes.Contact.route)
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Ver todos los productos") },
                    onClick = {
                        navController.navigate(Routes.Products.route)
                        menuExpanded = false
                    }
                )
            }
        }
        // ----> FIN DE LA MODIFICACIÓN <----
    )
}

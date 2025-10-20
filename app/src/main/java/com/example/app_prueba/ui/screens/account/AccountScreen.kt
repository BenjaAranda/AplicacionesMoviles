package com.example.app_prueba.ui.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.viewmodel.ProfileViewModel
import com.example.app_prueba.viewmodel.SessionViewModel

@Composable
fun AccountScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    val isLoggedIn = SessionViewModel.isLoggedIn
    val currentUserEmail = SessionViewModel.currentUserEmail

    // Vista para usuario que SÍ ha iniciado sesión
    if (isLoggedIn && currentUserEmail != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Mi Cuenta",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Hola, $currentUserEmail!",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // Navega a la página de contacto
                    navController.navigate(Routes.Contact.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "Soporte",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Contactar a Soporte Técnico")
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de logout hacia abajo

            Button(
                onClick = { profileViewModel.onLogout(navController) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
    // Vista para usuario que NO ha iniciado sesión
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Únete a la Comunidad",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Inicia sesión o crea una cuenta para acceder a tu carrito y perfil.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Routes.Login.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { navController.navigate(Routes.Register.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Cuenta")
            }
        }
    }
}
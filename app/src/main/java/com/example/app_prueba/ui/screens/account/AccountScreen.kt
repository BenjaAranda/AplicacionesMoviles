package com.example.app_prueba.ui.screens.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes
import com.example.app_prueba.viewmodel.SessionViewModel

// Función para contactar a soporte
private fun contactSupport(context: Context) {
    try {
        val phoneNumber = "+56912345678" // Reemplaza con un número de teléfono real
        val message = "Hola, necesito ayuda con la app Level-Up Gamer."

        val intent = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
        intent.data = Uri.parse(url)
        intent.setPackage("com.whatsapp") // Asegura que se abra en WhatsApp
        context.startActivity(intent)
    } catch (e: Exception) {
        // Esto ocurre si WhatsApp no está instalado
        Toast.makeText(context, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show()
    }
}

// Función para cerrar sesión
private fun onLogout(navController: NavController) {
    SessionViewModel.onLogout()
    navController.navigate(Routes.Login.route) {
        popUpTo(0) { // Elimina todo el backstack
            inclusive = true
        }
    }
}


@Composable
fun AccountScreen(navController: NavController) {
    val isLoggedIn = SessionViewModel.isLoggedIn
    val currentUserEmail = SessionViewModel.currentUserEmail
    val context = LocalContext.current

    if (isLoggedIn && currentUserEmail != null) {
        // --- VISTA PARA USUARIO LOGUEADO ---
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
                onClick = { contactSupport(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "Soporte",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Contactar a Soporte Técnico")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onLogout(navController) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar Sesión")
            }
        }
    } else {
        // --- VISTA PARA USUARIO NO LOGUEADO ---
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
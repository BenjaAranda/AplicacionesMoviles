package com.example.app_prueba.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.app_prueba.navigation.Routes // Asegúrate de importar tus rutas

class ProfileViewModel : ViewModel() {

    fun onLogout(navController: NavController) {
        // Lógica para cerrar sesión
        // Por ejemplo, limpiar preferencias, base de datos, etc.

        // Navegar a la pantalla de login y limpiar la pila de navegación
        navController.navigate(Routes.Login.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun contactSupport(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:+1234567890") // Reemplaza con el número de soporte
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir la aplicación de teléfono.", Toast.LENGTH_SHORT).show()
        }
    }
}

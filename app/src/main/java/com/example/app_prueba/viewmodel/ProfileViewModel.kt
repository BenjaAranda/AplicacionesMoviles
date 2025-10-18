package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.navigation.Routes
import kotlinx.coroutines.launch
import androidx.navigation.NavController

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // En un futuro, aquí obtendríamos los datos del usuario logueado.
    // Por ahora, solo nos enfocaremos en el logout.

    fun onLogout(navController: NavController) {
        // La lógica de logout es navegar al Login y limpiar toda la pila de navegación anterior.
        navController.navigate(Routes.Login.route) {
            popUpTo(0) { // Elimina todo el backstack
                inclusive = true
            }
        }
    }
}
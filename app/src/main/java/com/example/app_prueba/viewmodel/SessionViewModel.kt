package com.example.app_prueba.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

object SessionViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
        private set

    var currentUserEmail by mutableStateOf<String?>(null)
        private set

    var hasDuocDiscount by mutableStateOf(false)
        private set

    // --- NUEVO: Variable para guardar el Token JWT ---
    var userToken by mutableStateOf<String?>(null)
        private set

    // --- ACTUALIZADO: Ahora recibe el token ---
    fun onLoginSuccess(email: String, discount: Boolean, token: String?) {
        isLoggedIn = true
        currentUserEmail = email
        hasDuocDiscount = discount
        userToken = token // Guardamos el token para usarlo en el carrito
    }

    fun onLogout() {
        isLoggedIn = false
        currentUserEmail = null
        hasDuocDiscount = false
        userToken = null // Limpiamos el token al salir
    }
}
package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_prueba.data.model.UserRemote

object SessionViewModel {
    // --- IMPORTANTE: Deben ser variables públicas (var) ---
    var userToken: String? = null
    var loggedInUser: UserRemote? = null

    // Helper para saber si hay sesión
    val isLoggedIn: Boolean
        get() = userToken != null

    // Helper para saber si tiene descuento
    val hasDuocDiscount: Boolean
        get() = loggedInUser?.hasDuocDiscount == true
}
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

    fun onLoginSuccess(email: String) {
        isLoggedIn = true
        currentUserEmail = email
    }

    fun onLogout() {
        isLoggedIn = false
        currentUserEmail = null
    }
}
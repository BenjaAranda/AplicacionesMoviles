package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_prueba.data.model.UserRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserRemote?>(SessionViewModel.loggedInUser)
    val user = _user.asStateFlow()

    fun logout() {
        // Borramos los datos de sesión
        SessionViewModel.userToken = null
        SessionViewModel.loggedInUser = null
        _user.value = null
    }

    // Función para recargar datos si fuera necesario
    fun refreshUser() {
        _user.value = SessionViewModel.loggedInUser
    }
}
package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.User
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var confirmPass by mutableStateOf("")
    var registerError by mutableStateOf<String?>(null)

    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onRegister(onSuccess: () -> Unit) {
        if (pass != confirmPass) {
            registerError = "Las contraseñas no coinciden."
            return
        }

        viewModelScope.launch {
            if (userDao.getUserByEmail(email) != null) {
                registerError = "El correo electrónico ya está en uso."
                return@launch
            }

            val hasDuocDiscount = email.lowercase().endsWith("@duoc.cl") || email.lowercase().endsWith("@duocuc.cl")

            val newUser = User(
                email = email,
                pass = pass,
                hasDuocDiscount = hasDuocDiscount
            )

            userDao.insert(newUser)
            registerError = null
            // Inicia sesión automáticamente y guarda el estado del descuento
            SessionViewModel.onLoginSuccess(newUser.email, newUser.hasDuocDiscount)
            onSuccess()
        }
    }
}
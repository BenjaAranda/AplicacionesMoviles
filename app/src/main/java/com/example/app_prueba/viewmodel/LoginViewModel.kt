package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)

    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user == null) {
                loginError = "Usuario no encontrado."
            } else if (user.pass != pass) {
                loginError = "Contraseña incorrecta."
            } else {
                loginError = null
                SessionViewModel.onLoginSuccess(user.email) // Pasamos el email del usuario
                onSuccess()
            }
        }
    }
}
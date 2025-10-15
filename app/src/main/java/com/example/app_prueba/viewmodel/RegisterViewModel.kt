// ruta: viewmodel/RegisterViewModel.kt
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
        // Aquí irían más validaciones, como la edad, etc.

        viewModelScope.launch {
            // Verificar si el usuario ya existe
            if (userDao.getUserByEmail(email) != null) {
                registerError = "El correo electrónico ya está en uso."
                return@launch
            }

            // Lógica de descuento Duoc, requerida en el caso de estudio [cite: 107]
            val hasDuocDiscount = email.lowercase().endsWith("@duoc.cl") || email.lowercase().endsWith("@duocuc.cl")

            val newUser = User(
                email = email,
                pass = pass, // Recuerda: en un proyecto real, encriptar la contraseña
                hasDuocDiscount = hasDuocDiscount
            )

            userDao.insert(newUser)
            registerError = null
            onSuccess() // Navegación en caso de éxito
        }
    }
}
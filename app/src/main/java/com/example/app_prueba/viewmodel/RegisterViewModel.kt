package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.User
// Asegúrate de que SessionViewModel esté importado si no está en el mismo paquete
// import com.example.app_prueba.viewmodel.SessionViewModel
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var confirmPass by mutableStateOf("")
    var registerError by mutableStateOf<String?>(null)

    // --- Nuevos campos ---
    var isOfAge by mutableStateOf(false)
    var hasReferralCode by mutableStateOf(false)
    // --- CORRECCIÓN: 'mutableStateOf' con 'O' mayúscula ---
    var referralCode by mutableStateOf("")

    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onRegister(onSuccess: () -> Unit) {
        // Limpiar errores previos al iniciar una nueva validación
        registerError = null

        if (!isOfAge) {
            registerError = "Debes ser mayor de 18 años para registrarte."
            return
        }

        if (pass != confirmPass) {
            registerError = "Las contraseñas no coinciden."
            return
        }

        if (email.isBlank() || pass.isBlank()) {
            registerError = "El correo y la contraseña no pueden estar vacíos."
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
                // Si decides guardar el código de referido, deberás añadirlo al modelo 'User'
                // y pasarlo aquí: referralCode = referralCode
            )

            userDao.insert(newUser)

            // Inicia sesión automáticamente y guarda el estado del descuento
            // Nota: Asumiendo que SessionViewModel es un objeto singleton o tiene métodos estáticos.
            // Si no lo es, necesitarás una instancia.
            // SessionViewModel.onLoginSuccess(newUser.email, newUser.hasDuocDiscount)

            onSuccess()
        }
    }
}

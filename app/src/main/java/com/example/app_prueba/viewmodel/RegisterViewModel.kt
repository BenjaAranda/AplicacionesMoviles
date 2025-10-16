package com.example.app_prueba.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() { // O hereda de tu AuthViewModel si existe

    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var confirmPass by mutableStateOf("")

    // --- INICIO DE CAMPOS NUEVOS ---
    var isOfLegalAge by mutableStateOf(false)
    var isDuocStudent by mutableStateOf(false)
    // --- FIN DE CAMPOS NUEVOS ---

    var registerError by mutableStateOf<String?>(null)

    fun onRegister(onSuccess: () -> Unit) {
        registerError = null // Limpiar errores previos

        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            registerError = "Todos los campos son obligatorios."
            return
        }

        if (pass != confirmPass) {
            registerError = "Las contraseñas no coinciden."
            return
        }

        // --- INICIO DE VALIDACIONES NUEVAS ---

        if (!isOfLegalAge) {
            registerError = "Debes ser mayor de edad para registrarte."
            return
        }

        if (isDuocStudent && !email.endsWith("@duocuc.cl", ignoreCase = true)) {
            registerError = "Si eres estudiante Duoc, tu correo debe terminar en @duocuc.cl."
            return
        }

        // --- FIN DE VALIDACIONES NUEVAS ---

        // Aquí iría tu lógica de registro real (por ejemplo, con Firebase, una API, etc.)
        // Si el registro es exitoso, llamas a onSuccess()
        // Ejemplo simple:
        println("Registro exitoso para el correo: $email")
        onSuccess()

        // Si hubiera un error en el backend, harías algo como:
        // registerError = "Error del servidor: ${exception.message}"
    }
}

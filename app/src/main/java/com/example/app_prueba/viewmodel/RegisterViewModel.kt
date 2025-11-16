package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.User
import com.example.app_prueba.data.model.UserRegisterRequest
import com.example.app_prueba.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.IOException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var confirmPass by mutableStateOf("")
    var name by mutableStateOf("")
    var isOfAge by mutableStateOf(true)
    var hasReferralCode by mutableStateOf(false)
    var referralCode by mutableStateOf("")
    var registerError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    private val repository = UserRepository()
    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onRegister(onSuccess: () -> Unit) {
        // Validaciones locales antes de llamar al backend
        if (!isOfAge) {
            registerError = "Debes ser mayor de 18 años."
            return
        }
        if (email.isBlank()) {
            registerError = "El correo es obligatorio."
            return
        }
        if (!email.contains("@")) {
            registerError = "El correo no es válido."
            return
        }
        if (pass.length < 4) {
            registerError = "La contraseña debe tener mínimo 4 caracteres."
            return
        }
        if (pass != confirmPass) {
            registerError = "Las contraseñas no coinciden."
            return
        }

        // Prepara request
        val hasDuocDiscount = email.lowercase().endsWith("@duoc.cl") || email.lowercase().endsWith("@duocuc.cl")

        // --- INICIO DE CORRECCIÓN ---
        val request = UserRegisterRequest(
            email = email.trim().lowercase(),
            pass = pass,
            // Esto ahora es válido porque UserRegisterRequest.name es String?
            name = if (name.isBlank()) null else name.trim(),

            // CORREGIDO: El nombre del parámetro debe ser camelCase
            hasDuocDiscount = hasDuocDiscount
        )
        // --- FIN DE CORRECCIÓN ---

        viewModelScope.launch {
            isLoading = true
            registerError = null
            try {
                // NOTA: El backend /api/register devuelve un 'AuthResponse' que en tu
                // app.py SÍ tiene un token. Asumo que tu 'repository.registerUser'
                // usa el 'AuthResponse' que definimos arriba.
                val response = repository.registerUser(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        // guardar usuario localmente (Room) para sesión offline
                        val newUser = User(
                            email = request.email,
                            pass = request.pass,
                            name = request.name, // 'name' ya es String?
                            hasDuocDiscount = hasDuocDiscount
                        )
                        userDao.insert(newUser)

                        // Guardar sesión
                        SessionViewModel.onLoginSuccess(newUser.email, newUser.hasDuocDiscount)

                        registerError = null
                        onSuccess()
                    } else {
                        registerError = body.message
                    }
                } else {
                    registerError = "Error del servidor: ${response.code()} ${response.message()}"
                }
            } catch (e: IOException) {
                registerError = "Error de red: No se pudo conectar al servidor."
            } catch (e: Exception) {
                registerError = "Error inesperado: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
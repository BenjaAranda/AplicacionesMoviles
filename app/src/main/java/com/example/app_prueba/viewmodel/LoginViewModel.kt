package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.LoginRequest
import com.example.app_prueba.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    private val repository = UserRepository()
    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onLogin(onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            loginError = "Correo y contraseña obligatorios."
            return
        }

        viewModelScope.launch {
            isLoading = true
            loginError = null
            try {
                val request = LoginRequest(email.trim().lowercase(), pass)
                val response = repository.loginUser(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!! // body es AuthResponse

                    // --- INICIO DE CORRECCIÓN ---
                    // Ahora accedemos a 'body.data.token' y 'body.data.user'
                    if (body.success && body.data?.token != null) {

                        val userFromResponse = body.data.user
                        var userEmail = request.email
                        var userHasDuoc = false

                        if (userFromResponse != null) {
                            // Si el backend nos dio un usuario, lo usamos
                            val localUser = com.example.app_prueba.data.model.User(
                                email = userFromResponse.email,
                                pass = pass, // Guardamos el pass que el usuario tipeó
                                name = userFromResponse.name, // 'name' ya es String?
                                hasDuocDiscount = userFromResponse.hasDuocDiscount
                            )
                            userDao.insert(localUser)

                            userEmail = localUser.email
                            userHasDuoc = localUser.hasDuocDiscount
                        } else {
                            // Fallback por si 'user' es nulo
                            userHasDuoc = userEmail.endsWith("@duoc.cl") || userEmail.endsWith("@duocuc.cl")
                        }

                        // Manejar sesión global
                        SessionViewModel.onLoginSuccess(userEmail, userHasDuoc)

                        loginError = null
                        onSuccess()
                        // --- FIN DE CORRECCIÓN ---
                    } else {
                        loginError = body.message
                    }
                } else {
                    loginError = "Error del servidor: ${response.code()} ${response.message()}"
                }
            } catch (e: IOException) {
                loginError = "Error de red: No se pudo conectar al servidor."
            } catch (e: Exception) {
                loginError = "Error inesperado: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
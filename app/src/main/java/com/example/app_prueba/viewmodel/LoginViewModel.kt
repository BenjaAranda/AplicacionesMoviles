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

    // --- CAMBIO CLAVE PARA TESTING ---
    // Usamos 'var' para que el Test pueda inyectar un Mock (falso) aquí.
    var userRepository: UserRepository = UserRepository()

    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    // --- ELIMINADO: private val repository = UserRepository() ---
    // Ya no lo necesitamos porque usaremos 'userRepository' de arriba.

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

                // --- USO DE LA VARIABLE CORRECTA ---
                // Ahora usamos 'userRepository', así el test puede controlar la respuesta.
                val response = userRepository.loginUser(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    // Verificamos éxito y que exista el token
                    if (body.success && body.data?.token != null) {

                        val userFromResponse = body.data.user
                        var userEmail = request.email
                        var userHasDuoc = false

                        if (userFromResponse != null) {
                            val localUser = com.example.app_prueba.data.model.User(
                                email = userFromResponse.email,
                                pass = pass,
                                name = userFromResponse.name,
                                hasDuocDiscount = userFromResponse.hasDuocDiscount
                            )
                            userDao.insert(localUser)

                            userEmail = localUser.email
                            userHasDuoc = localUser.hasDuocDiscount
                        } else {
                            userHasDuoc = userEmail.endsWith("@duoc.cl") || userEmail.endsWith("@duocuc.cl")
                        }

                        // Guardamos sesión
                        SessionViewModel.onLoginSuccess(
                            email = userEmail,
                            discount = userHasDuoc,
                            token = body.data.token
                        )

                        loginError = null
                        onSuccess()
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
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
    var registerError by mutableStateOf<String?>(null)

    private val repository = UserRepository()
    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onRegister(onSuccess: () -> Unit) {
        if (pass != confirmPass) {
            registerError = "Las contraseñas no coinciden."
            return
        }

        val hasDuocDiscount = email.lowercase().endsWith("@duoc.cl") || email.lowercase().endsWith("@duocuc.cl")
        val userRequest = UserRegisterRequest(
            email = email,
            pass = pass,
            hasDuocDiscount = hasDuocDiscount
        )

        viewModelScope.launch {
            try {
                val response = repository.registerUser(userRequest)

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.success) {
                        registerError = null

                        // Guardamos el usuario en la BD local (Room) para la sesión
                        val newUser = User(
                            email = userRequest.email,
                            pass = userRequest.pass,
                            hasDuocDiscount = userRequest.hasDuocDiscount
                        )
                        userDao.insert(newUser)

                        SessionViewModel.onLoginSuccess(newUser.email, newUser.hasDuocDiscount)
                        onSuccess()
                    } else {
                        registerError = response.body()?.message ?: "Error desconocido"
                    }
                } else {
                    registerError = "Error del servidor: ${response.code()}"
                }

            } catch (e: IOException) {
                registerError = "Error de red: No se pudo conectar al servidor."
            } catch (e: Exception) {
                registerError = "Error inesperado: ${e.message}"
            }
        }
    }
}
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

// NOTA EL CAMBIO AQUÍ: Agregamos 'userRepository' al paréntesis del constructor
class RegisterViewModel(
    application: Application,
    private val userRepository: UserRepository = UserRepository() // <-- CRUCIAL: Valor por defecto para no romper el resto de la app
) : AndroidViewModel(application) {

    // --- IMPORTANTE: ELIMINA LA LÍNEA ANTIGUA QUE DECÍA: ---
    // private val repository = UserRepository()
    // (Si la dejas, el test fallará siempre)

    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var confirmPass by mutableStateOf("")
    var name by mutableStateOf("")
    var isOfAge by mutableStateOf(true)
    var hasReferralCode by mutableStateOf(false)
    var referralCode by mutableStateOf("")
    var registerError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    private val userDao = (application as LevelUpGamerApp).database.userDao()

    fun onRegister(onSuccess: () -> Unit) {
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

        val hasDuocDiscount = email.lowercase().endsWith("@duoc.cl") || email.lowercase().endsWith("@duocuc.cl")

        val request = UserRegisterRequest(
            email = email.trim().lowercase(),
            pass = pass,
            name = if (name.isBlank()) null else name.trim(),
            hasDuocDiscount = hasDuocDiscount
        )

        viewModelScope.launch {
            isLoading = true
            registerError = null
            try {
                // USAMOS LA VARIABLE DEL CONSTRUCTOR (userRepository)
                val response = userRepository.registerUser(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        val newUser = User(
                            email = request.email,
                            pass = request.pass,
                            name = request.name,
                            hasDuocDiscount = hasDuocDiscount
                        )
                        userDao.insert(newUser)

                        SessionViewModel.onLoginSuccess(
                            email = newUser.email,
                            discount = newUser.hasDuocDiscount,
                            token = body.data?.token
                        )

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
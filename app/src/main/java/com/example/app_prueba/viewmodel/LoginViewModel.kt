package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.data.model.*
import com.example.app_prueba.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            try {
                val response = repository.loginUser(email, pass)
                if (response.isSuccessful && response.body()?.success == true) {
                    // Guardar datos de sesión
                    val responseData = response.body()?.data
                    SessionViewModel.userToken = responseData?.token
                    SessionViewModel.loggedInUser = responseData?.user

                    _uiState.value = LoginUiState(isSuccess = true)
                } else {
                    val errorMsg = response.body()?.message ?: "Error en credenciales"
                    _uiState.value = LoginUiState(error = errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState(error = "Error de conexión: ${e.message}")
            }
        }
    }
}
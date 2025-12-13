package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.data.model.*
import com.example.app_prueba.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun register(email: String, pass: String, name: String, hasDuocDiscount: Boolean) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            try {
                // Usamos la nueva función del repositorio
                val response = repository.registerUser(email, pass, name, hasDuocDiscount)

                if (response.isSuccessful && response.body()?.success == true) {
                    // Si el registro devuelve token automáticamente, logueamos
                    val responseData = response.body()?.data
                    if (responseData != null) {
                        SessionViewModel.userToken = responseData.token
                        SessionViewModel.loggedInUser = responseData.user
                    }
                    _uiState.value = RegisterUiState(isSuccess = true)
                } else {
                    val msg = response.body()?.message ?: "Error al registrar"
                    _uiState.value = RegisterUiState(error = msg)
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState(error = "Error de red: ${e.message}")
            }
        }
    }
}
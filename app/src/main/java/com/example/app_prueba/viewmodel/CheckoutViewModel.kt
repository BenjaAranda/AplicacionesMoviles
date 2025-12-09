package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado de la pantalla de Checkout
data class CheckoutState(
    val address: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

enum class PaymentMethod {
    CREDIT_CARD, DEBIT_CARD, CASH
}

class CheckoutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutState())
    val uiState = _uiState.asStateFlow()

    // Funciones para actualizar los campos
    fun updateAddress(newAddress: String) {
        _uiState.value = _uiState.value.copy(address = newAddress)
    }

    fun updateCity(newCity: String) {
        _uiState.value = _uiState.value.copy(city = newCity)
    }

    fun updatePhone(newPhone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = newPhone)
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(paymentMethod = method)
    }

    // Función para procesar la compra
    fun processPayment(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validación simple
        if (state.address.isBlank() || state.city.isBlank() || state.phoneNumber.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Por favor completa todos los campos de envío.")
            return
        }

        // Simular proceso de pago (Loading)
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            delay(2000) // Simula 2 segundos de conexión al banco
            _uiState.value = state.copy(isLoading = false, isSuccess = true)
            onSuccess() // Ejecuta la limpieza del carrito y navegación
        }
    }
}
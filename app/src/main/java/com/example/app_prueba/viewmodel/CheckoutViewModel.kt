package com.example.app_prueba.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PaymentMethod {
    CREDIT_CARD, DEBIT_CARD, CASH
}

data class CheckoutState(
    val address: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class CheckoutViewModel : ViewModel() {
    private val _uiState = mutableStateOf(CheckoutState())
    // Exponemos como StateFlow para Compose (adaptador simple)
    val uiState = kotlinx.coroutines.flow.MutableStateFlow(CheckoutState())

    private val repository = ProductRepository()

    fun updateAddress(newAddress: String) {
        updateState { it.copy(address = newAddress) }
    }

    fun updateCity(newCity: String) {
        updateState { it.copy(city = newCity) }
    }

    fun updatePhone(newPhone: String) {
        updateState { it.copy(phoneNumber = newPhone) }
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        updateState { it.copy(paymentMethod = method) }
    }

    private fun updateState(update: (CheckoutState) -> CheckoutState) {
        val newState = update(uiState.value)
        uiState.value = newState
    }

    fun processPayment(onSuccess: () -> Unit) {
        val currentState = uiState.value

        if (currentState.address.isBlank() || currentState.city.isBlank() || currentState.phoneNumber.isBlank()) {
            updateState { it.copy(errorMessage = "Por favor completa todos los campos de envío.") }
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }

            val token = SessionViewModel.userToken
            if (token != null) {
                try {
                    // Llamada al Backend para crear la orden
                    val response = repository.checkout(token)

                    if (response.isSuccessful && response.body()?.success == true) {
                        updateState { it.copy(isLoading = false, isSuccess = true) }
                        onSuccess()
                    } else {
                        updateState { it.copy(isLoading = false, errorMessage = "Error en el pago: ${response.message()}") }
                    }
                } catch (e: Exception) {
                    updateState { it.copy(isLoading = false, errorMessage = "Error de conexión con el banco (API).") }
                }
            } else {
                updateState { it.copy(isLoading = false, errorMessage = "Sesión expirada. Ingresa nuevamente.") }
            }
        }
    }
}
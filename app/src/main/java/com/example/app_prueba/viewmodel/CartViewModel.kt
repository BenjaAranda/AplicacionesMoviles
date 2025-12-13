package com.example.app_prueba.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false
)

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CartState())
    val uiState = _uiState.asStateFlow()

    private val repository = ProductRepository()
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val token = SessionViewModel.userToken

            if (token != null) {
                try {
                    val response = repository.getCart(token)
                    if (response.isSuccessful && response.body()?.success == true) {
                        val remoteItems = response.body()?.data ?: emptyList()

                        // Mapeamos respuesta del servidor a modelo local
                        val mappedItems = remoteItems.map {
                            CartItem(
                                id = it.id, // ID del item en el carrito (Backend)
                                productCode = it.productId.toString(),
                                productName = it.name,
                                productPrice = it.price,
                                quantity = it.quantity,
                                imageRes = 0
                            )
                        }
                        updatePrices(mappedItems)
                    }
                } catch (e: Exception) {
                    Log.e("CartVM", "Error cargando", e)
                }
            } else {
                // Modo offline (opcional, limpia por ahora)
                _uiState.value = CartState()
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private fun updatePrices(items: List<CartItem>) {
        val subtotal = items.sumOf { it.productPrice * it.quantity }
        val discount = if (SessionViewModel.hasDuocDiscount) subtotal * 0.20 else 0.0
        val total = subtotal - discount

        _uiState.value = _uiState.value.copy(
            items = items,
            subtotal = subtotal,
            discount = discount,
            total = total
        )
    }

    // --- LÓGICA DE BOTONES ---

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            val token = SessionViewModel.userToken ?: return@launch
            // Optimistic update (actualiza UI rápido) o espera response.
            // Aquí esperamos response para asegurar consistencia
            try {
                val newQty = item.quantity + 1
                // Llamamos al PUT que creamos en backend
                val res = repository.updateCartItemQuantity(token, item.id, newQty)
                if (res.isSuccessful) {
                    loadCart() // Recargamos para ver cambios
                }
            } catch (e: Exception) {
                Log.e("CartVM", "Error aumentando", e)
            }
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            val token = SessionViewModel.userToken ?: return@launch
            try {
                if (item.quantity > 1) {
                    // Si hay más de 1, restamos
                    val res = repository.updateCartItemQuantity(token, item.id, item.quantity - 1)
                    if (res.isSuccessful) loadCart()
                } else {
                    // Si es 1, BORRAMOS el item
                    val res = repository.deleteCartItem(token, item.id)
                    if (res.isSuccessful) loadCart()
                }
            } catch (e: Exception) {
                Log.e("CartVM", "Error disminuyendo", e)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            val token = SessionViewModel.userToken

            // 1. Borrar en la nube
            if (token != null) {
                try {
                    repository.clearCartCloud(token)
                } catch (e: Exception) {
                    Log.e("CartVM", "Error vaciando nube", e)
                }
            }

            // 2. Borrar local
            cartDao.deleteAll()

            // 3. Actualizar UI
            _uiState.value = CartState()
        }
    }
}
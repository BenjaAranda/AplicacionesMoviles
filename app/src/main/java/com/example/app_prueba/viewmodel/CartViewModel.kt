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

    private val productRepository = ProductRepository()
    // Mantenemos el DAO local para vaciarlo al cerrar sesión o como respaldo
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val token = SessionViewModel.userToken

            if (token != null) {
                // --- MODO ONLINE: Cargar desde AWS ---
                try {
                    val response = productRepository.getCart(token)
                    if (response.isSuccessful && response.body()?.success == true) {
                        val remoteItems = response.body()?.data ?: emptyList()

                        // Convertimos los items del backend a nuestro modelo CartItem
                        val mappedItems = remoteItems.map {
                            CartItem(
                                id = it.id,
                                productCode = it.productId.toString(), // Usamos ID como código
                                productName = it.name,
                                productPrice = it.price,
                                quantity = it.quantity,
                                imageRes = 0 // La imagen se resolverá en la vista por nombre
                            )
                        }
                        updatePrices(mappedItems)
                    } else {
                        Log.e("CartVM", "Error cargando carrito nube: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("CartVM", "Error de red carrito", e)
                }
            } else {
                // --- MODO OFFLINE: Cargar local (Opcional) ---
                // Si no hay login, podrías cargar del DAO local
                _uiState.value = CartState()
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Calcular totales
    private fun updatePrices(items: List<CartItem>) {
        val subtotal = items.sumOf { it.productPrice * it.quantity }

        // Descuento DUOC (20%) si aplica
        val discount = if (SessionViewModel.hasDuocDiscount) subtotal * 0.20 else 0.0
        val total = subtotal - discount

        _uiState.value = _uiState.value.copy(
            items = items,
            subtotal = subtotal,
            discount = discount,
            total = total
        )
    }

    // Funciones de modificar cantidad (Por ahora solo local o simulado,
    // ya que requeriría endpoints de UPDATE en el backend)
    fun increaseQuantity(item: CartItem) {
        // Implementación ideal: llamar a API update quantity
        // Por ahora, recargamos para no desincronizar
        loadCart()
    }

    fun decreaseQuantity(item: CartItem) {
        loadCart()
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.deleteAll() // Limpia local
            _uiState.value = CartState() // Limpia UI
            // Nota: Para limpiar la nube necesitarías un endpoint DELETE /cart
        }
    }
}
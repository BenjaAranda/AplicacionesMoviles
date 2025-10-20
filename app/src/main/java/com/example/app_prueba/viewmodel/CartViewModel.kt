package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CartState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0
)

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CartState())
    val uiState = _uiState.asStateFlow()

    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartDao.getAllItems().collectLatest { items ->
                val subtotal = items.sumOf { it.productPrice * it.quantity }
                val discount = if (SessionViewModel.hasDuocDiscount) subtotal * 0.20 else 0.0
                val total = subtotal - discount

                _uiState.value = CartState(
                    items = items,
                    subtotal = subtotal,
                    discount = discount,
                    total = total
                )
            }
        }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            item.quantity++
            cartDao.upsertItem(item)
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                item.quantity--
                cartDao.upsertItem(item)
            } else {
                // Si la cantidad es 1, eliminar el producto
                cartDao.deleteItem(item)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            // Esta es una nueva función que debemos añadir al DAO
            cartDao.clearAllItems()
        }
    }
}
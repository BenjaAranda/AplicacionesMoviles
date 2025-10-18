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
                val total = items.sumOf { it.productPrice * it.quantity }
                _uiState.value = CartState(items = items, total = total)
            }
        }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            cartDao.deleteItem(item)
        }
    }
}
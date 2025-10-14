// ruta: viewmodel/HomeViewModel.kt
package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val productDao = (application as LevelUpGamerApp).database.productDao()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collectLatest { productList ->
                _uiState.value = HomeState(products = productList, isLoading = false)
            }
        }
    }
}
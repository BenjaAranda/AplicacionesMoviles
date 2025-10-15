package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = true
)

class ProductDetailViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState = _uiState.asStateFlow()

    private val productDao = (application as LevelUpGamerApp).database.productDao()

    init {
        val productId: String? = savedStateHandle["productId"]
        if (productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            productDao.getProductByCode(productId).collectLatest { product ->
                _uiState.value = ProductDetailState(product = product, isLoading = false)
            }
        }
    }
}
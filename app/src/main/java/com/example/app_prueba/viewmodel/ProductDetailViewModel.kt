package com.example.app_prueba.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ProductDetailViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState = _uiState.asStateFlow()

    private val repository = ProductRepository()
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    init {
        val productIdString: String? = savedStateHandle["productId"]
        if (productIdString != null) {
            val id = productIdString.toIntOrNull()
            if (id != null) {
                loadProductFromApi(id)
            } else {
                _uiState.value = ProductDetailState(isLoading = false, error = "Producto no encontrado")
            }
        }
    }

    private fun loadProductFromApi(id: Int) {
        viewModelScope.launch {
            try {
                // --- CORRECCIÓN: Usamos la función pública del repositorio ---
                val response = repository.getProductById(id)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = ProductDetailState(
                        product = response.body()!!.data,
                        isLoading = false
                    )
                } else {
                    loadFromListFallback(id)
                }
            } catch (e: Exception) {
                Log.e("DetailVM", "Error cargando detalle", e)
                loadFromListFallback(id)
            }
        }
    }

    private suspend fun loadFromListFallback(id: Int) {
        try {
            val listResponse = repository.getProductsFromApi()
            val found = listResponse.body()?.data?.find { it.id == id }
            if (found != null) {
                _uiState.value = ProductDetailState(product = found, isLoading = false)
            } else {
                _uiState.value = ProductDetailState(isLoading = false, error = "No se encontró")
            }
        } catch (e: Exception) {
            _uiState.value = ProductDetailState(isLoading = false, error = "Error de conexión")
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val token = SessionViewModel.userToken

            if (token != null) {
                try {
                    repository.addToCart(token, product.id, 1)
                    Log.d("DetailVM", "Añadido al carrito nube")
                } catch (e: Exception) {
                    Log.e("DetailVM", "Error añadiendo a nube", e)
                }
            }

            val code = if(product.code != null && product.code.isNotEmpty()) product.code else product.id.toString()

            val existingItem = cartDao.getItemByCode(code)
            if (existingItem != null) {
                existingItem.quantity++
                cartDao.upsertItem(existingItem)
            } else {
                val cartItem = CartItem(
                    productCode = code,
                    productName = product.name ?: "Producto",
                    productPrice = product.price,
                    quantity = 1,
                    // Llenamos los campos nuevos (id backend y recurso imagen local por defecto)
                    id = product.id,
                    imageRes = 0
                )
                cartDao.upsertItem(cartItem)
            }
        }
    }
}
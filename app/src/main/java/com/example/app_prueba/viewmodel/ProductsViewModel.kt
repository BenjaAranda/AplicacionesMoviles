package com.example.app_prueba.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SortOption {
    DEFAULT, PRICE_ASC, PRICE_DESC, NAME_ASC
}

data class ProductsState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "Todas",
    val minPrice: String = "",
    val maxPrice: String = "",
    val sortOption: SortOption = SortOption.DEFAULT
)

class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState = _uiState.asStateFlow()

    private val productRepository = ProductRepository()
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()
    private var allProductsCache: List<Product> = emptyList()

    init {
        loadProductsFromBackend()
    }

    private fun loadProductsFromBackend() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = productRepository.getProductsFromApi()

                if (response.isSuccessful && response.body()?.success == true) {
                    val remoteProducts = response.body()?.data ?: emptyList()
                    allProductsCache = remoteProducts

                    val categories = mutableListOf("Todas")
                    categories.addAll(remoteProducts.map { it.category ?: "General" }.distinct().sorted())

                    _uiState.value = _uiState.value.copy(
                        products = remoteProducts,
                        categories = categories,
                        isLoading = false
                    )
                } else {
                    Log.e("ProductsVM", "Error cargando productos: ${response.code()}")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                Log.e("ProductsVM", "Excepción de red", e)
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // --- FILTROS ---
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    fun onMinPriceChange(price: String) {
        if (price.all { it.isDigit() }) _uiState.value = _uiState.value.copy(minPrice = price)
    }

    fun onMaxPriceChange(price: String) {
        if (price.all { it.isDigit() }) _uiState.value = _uiState.value.copy(maxPrice = price)
    }

    fun onSortOptionChange(option: SortOption) {
        _uiState.value = _uiState.value.copy(sortOption = option)
        applyFilters()
    }

    fun applyFilters() {
        val state = _uiState.value
        var filteredList = allProductsCache

        if (state.searchQuery.isNotBlank()) {
            filteredList = filteredList.filter {
                (it.name ?: "").contains(state.searchQuery, ignoreCase = true)
            }
        }

        if (state.selectedCategory != "Todas") {
            filteredList = filteredList.filter {
                (it.category ?: "General").equals(state.selectedCategory, ignoreCase = true)
            }
        }

        val min = state.minPrice.toDoubleOrNull()
        val max = state.maxPrice.toDoubleOrNull()

        if (min != null) filteredList = filteredList.filter { it.price >= min }
        if (max != null) filteredList = filteredList.filter { it.price <= max }

        filteredList = when (state.sortOption) {
            SortOption.PRICE_ASC -> filteredList.sortedBy { it.price }
            SortOption.PRICE_DESC -> filteredList.sortedByDescending { it.price }
            SortOption.NAME_ASC -> filteredList.sortedBy { it.name }
            SortOption.DEFAULT -> filteredList
        }

        _uiState.value = state.copy(products = filteredList)
    }

    // --- AGREGAR AL CARRITO (CORREGIDO) ---
    fun addToCart(product: Product) {
        viewModelScope.launch {
            val token = SessionViewModel.userToken

            // 1. Intentar Backend
            if (!token.isNullOrEmpty()) {
                try {
                    val response = productRepository.addToCart(token, product.id, 1)
                    if(response.isSuccessful) {
                        Log.d("ProductsVM", "Agregado al carrito nube OK")
                    } else {
                        Log.e("ProductsVM", "Error backend carrito: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("ProductsVM", "Error red carrito", e)
                }
            }

            // 2. Guardar Local (CORRECCIÓN CRÍTICA DE CRASH)
            try {
                // Verificamos si code es nulo. Si lo es, usamos el ID.
                val safeCode = if (product.code != null && product.code.isNotEmpty()) {
                    product.code
                } else {
                    product.id.toString()
                }

                val safeName = product.name ?: "Producto sin nombre"

                val existingItem = cartDao.getItemByCode(safeCode)
                if (existingItem != null) {
                    existingItem.quantity++
                    cartDao.upsertItem(existingItem)
                } else {
                    val cartItem = CartItem(
                        id = product.id, // ID backend
                        productCode = safeCode,
                        productName = safeName,
                        productPrice = product.price,
                        quantity = 1,
                        imageRes = 0
                    )
                    cartDao.upsertItem(cartItem)
                }
                Log.d("ProductsVM", "Guardado en DB local exitosamente")
            } catch (e: Exception) {
                Log.e("ProductsVM", "Error fatal guardando en local DB", e)
            }
        }
    }
}
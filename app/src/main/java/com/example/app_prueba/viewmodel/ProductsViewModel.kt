package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle // Import necesario
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.example.app_prueba.data.database.APP_CATEGORIES_LIST


enum class SortOption {
    DEFAULT,
    PRICE_ASC,
    PRICE_DESC,
    NAME_ASC
}

data class ProductsState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val selectedCategory: String = "Todas",
    val categories: List<String> = emptyList(),
    val minPrice: String = "",
    val maxPrice: String = "",
    val sortOption: SortOption = SortOption.DEFAULT
)

// --- CAMBIO 1: El constructor AHORA RECIBE SavedStateHandle ---
class ProductsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle // <--- ¡YA ESTÁ AQUÍ!
) : AndroidViewModel(application) {

    // --- CAMBIO 2: LEEMOS la categoría que viene de la navegación ---
    private val categoryFromNav: String? = savedStateHandle.get<String>("category")

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState = _uiState.asStateFlow()

    private val productDao = (application as LevelUpGamerApp).database.productDao()
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()
    private val allProductsFlow = productDao.getAllProducts()

    // --- CAMBIO 3: USAMOS la categoría de la navegación (categoryFromNav) para el valor inicial ---
    private val filtersFlow = MutableStateFlow(Triple(
        "",
        categoryFromNav ?: "Todas", // <-- Si es null, usa "Todas"
        SortOption.DEFAULT
    ))

    init {
        viewModelScope.launch {
            combine(allProductsFlow, filtersFlow) { products, (query, category, sort) ->

                val availableCategories = listOf("Todas") + APP_CATEGORIES_LIST

                val filteredProducts = products.filter { product ->
                    val categoryMatch = category == "Todas" || product.category == category
                    val queryMatch = query.isBlank() || product.name.contains(query, ignoreCase = true)

                    val minPrice = _uiState.value.minPrice.toDoubleOrNull() ?: 0.0
                    val maxPrice = _uiState.value.maxPrice.toDoubleOrNull() ?: Double.MAX_VALUE
                    val priceMatch = product.price in minPrice..maxPrice

                    categoryMatch && queryMatch && priceMatch
                }

                val sortedProducts = when (sort) {
                    SortOption.PRICE_ASC -> filteredProducts.sortedBy { it.price }
                    SortOption.PRICE_DESC -> filteredProducts.sortedByDescending { it.price }
                    SortOption.NAME_ASC -> filteredProducts.sortedBy { it.name }
                    SortOption.DEFAULT -> filteredProducts
                }

                _uiState.value = _uiState.value.copy(
                    products = sortedProducts,
                    isLoading = false,
                    searchQuery = query,
                    selectedCategory = category ?: "Todas",
                    categories = availableCategories
                )
            }.collect {}
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onMinPriceChange(price: String) {
        _uiState.value = _uiState.value.copy(minPrice = price)
    }

    fun onMaxPriceChange(price: String) {
        _uiState.value = _uiState.value.copy(maxPrice = price)
    }

    fun onSortOptionChange(sortOption: SortOption) {
        _uiState.value = _uiState.value.copy(sortOption = sortOption)
    }

    fun applyFilters() {
        filtersFlow.value = Triple(_uiState.value.searchQuery, _uiState.value.selectedCategory, _uiState.value.sortOption)
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existingItem = cartDao.getItemByCode(product.code)
            if (existingItem != null) {
                existingItem.quantity++
                cartDao.upsertItem(existingItem)
            } else {
                val cartItem = CartItem(
                    productCode = product.code,
                    productName = product.name,
                    productPrice = product.price,
                    quantity = 1
                )
                cartDao.upsertItem(cartItem)
            }
        }
    }
}
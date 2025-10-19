package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList()
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val productDao = (application as LevelUpGamerApp).database.productDao()
    private val allProductsFlow = productDao.getAllProducts()

    private val searchQueryFlow = MutableStateFlow("")
    private val selectedCategoryFlow = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            // Combina los flujos: la lista de productos, la búsqueda y el filtro
            combine(allProductsFlow, searchQueryFlow, selectedCategoryFlow) { products, query, category ->
                val availableCategories = products.map { it.category }.distinct()

                val filteredProducts = products.filter { product ->
                    // Filtro por categoría
                    val categoryMatch = category == null || product.category == category
                    // Filtro por texto de búsqueda
                    val queryMatch = query.isBlank() ||
                            product.name.contains(query, ignoreCase = true) ||
                            product.description.contains(query, ignoreCase = true)
                    categoryMatch && queryMatch
                }

                _uiState.value = HomeState(
                    products = filteredProducts,
                    isLoading = false,
                    searchQuery = query,
                    selectedCategory = category,
                    categories = listOf("Todas") + availableCategories // Añade "Todas" como opción
                )
            }.collect {}
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQueryFlow.value = query
    }

    fun onCategorySelected(category: String?) {
        selectedCategoryFlow.value = if (category == "Todas") null else category
    }
}
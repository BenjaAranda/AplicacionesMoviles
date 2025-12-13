package com.example.app_prueba.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.repository.ProductRepository // Usamos el repositorio del Backend
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

    // Repositorio para conectar con AWS
    private val productRepository = ProductRepository()

    // DAO local solo para el carrito (offline support si quisieras) o respaldo
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    // Guardamos la lista COMPLETA original para poder filtrar sobre ella
    private var allProductsCache: List<Product> = emptyList()

    init {
        loadProductsFromBackend()
    }

    private fun loadProductsFromBackend() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Llamada a la API (AWS)
                val response = productRepository.getProductsFromApi()

                if (response.isSuccessful && response.body()?.success == true) {
                    val remoteProducts = response.body()?.data ?: emptyList()

                    // Guardamos la lista original
                    allProductsCache = remoteProducts

                    // Extraemos categorías dinámicamente de los productos recibidos
                    val categories = mutableListOf("Todas")
                    categories.addAll(remoteProducts.map { it.category }.distinct().sorted())

                    // Actualizamos la UI con los datos sin filtrar iniciales
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

    // --- MANEJO DE FILTROS ---

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    fun onMinPriceChange(price: String) {
        // Solo permitir números
        if (price.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(minPrice = price)
        }
    }

    fun onMaxPriceChange(price: String) {
        if (price.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(maxPrice = price)
        }
    }

    fun onSortOptionChange(option: SortOption) {
        _uiState.value = _uiState.value.copy(sortOption = option)
        applyFilters()
    }

    fun applyFilters() {
        val state = _uiState.value
        var filteredList = allProductsCache

        // 1. Filtro por Buscador (Nombre)
        if (state.searchQuery.isNotBlank()) {
            filteredList = filteredList.filter {
                it.name.contains(state.searchQuery, ignoreCase = true)
            }
        }

        // 2. Filtro por Categoría
        if (state.selectedCategory != "Todas") {
            filteredList = filteredList.filter {
                it.category.equals(state.selectedCategory, ignoreCase = true)
            }
        }

        // 3. Filtro por Precio
        val min = state.minPrice.toDoubleOrNull()
        val max = state.maxPrice.toDoubleOrNull()

        if (min != null) {
            filteredList = filteredList.filter { it.price >= min }
        }
        if (max != null) {
            filteredList = filteredList.filter { it.price <= max }
        }

        // 4. Ordenamiento
        filteredList = when (state.sortOption) {
            SortOption.PRICE_ASC -> filteredList.sortedBy { it.price }
            SortOption.PRICE_DESC -> filteredList.sortedByDescending { it.price }
            SortOption.NAME_ASC -> filteredList.sortedBy { it.name }
            SortOption.DEFAULT -> filteredList // Mantiene el orden por ID (defecto de BD)
        }

        _uiState.value = state.copy(products = filteredList)
    }

    // --- AGREGAR AL CARRITO (Conectado a AWS) ---
    fun addToCart(product: Product) {
        viewModelScope.launch {
            val token = SessionViewModel.userToken

            // Intento 1: Mandar a la nube
            if (!token.isNullOrEmpty()) {
                try {
                    productRepository.addToCart(token, product.id, 1)
                    Log.d("ProductsVM", "Agregado al carrito nube: ${product.name}")
                } catch (e: Exception) {
                    Log.e("ProductsVM", "Error agregando al carrito nube", e)
                }
            }

            // Intento 2: Guardar local (siempre útil como feedback inmediato o respaldo)
            try {
                // Usamos el ID como código para consistencia
                val code = if (product.code.isNotEmpty()) product.code else product.id.toString()

                val existingItem = cartDao.getItemByCode(code)
                if (existingItem != null) {
                    existingItem.quantity++
                    cartDao.upsertItem(existingItem)
                } else {
                    val cartItem = CartItem(
                        productCode = code,
                        productName = product.name,
                        productPrice = product.price,
                        quantity = 1
                    )
                    cartDao.upsertItem(cartItem)
                }
            } catch (e: Exception) {
                Log.e("ProductsVM", "Error local DB", e)
            }
        }
    }
}
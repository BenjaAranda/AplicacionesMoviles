package com.example.app_prueba.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.R
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.repository.ProductRepository
import com.example.app_prueba.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

// Modelo local para la vista
data class ProductCategory(
    val name: String,
    @DrawableRes val imageRes: Int
)

data class HomeState(
    val featuredProducts: List<Product> = emptyList(),
    val categories: List<ProductCategory> = emptyList(),
    val isLoading: Boolean = true,
    val pokemonName: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    // DAOs locales
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    // Repositorios
    private val userRepository = UserRepository()
    private val productRepository = ProductRepository()

    init {
        loadHomePageContent()
    }

    private fun loadHomePageContent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // 1. CARGA DE API EXTERNA (PokeAPI - Restaurado)
            val pokemonNameResult: String? = try {
                // Ahora UserRepository sí tiene getDitto()
                val response = userRepository.getDitto()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                } else {
                    "Error al cargar Pokémon"
                }
            } catch (e: Exception) {
                "Error PokeAPI: ${e.message}"
            }

            // 2. CARGA DE PRODUCTOS (BACKEND AWS)
            var featured: List<Product> = emptyList()
            var categoriesList: List<ProductCategory> = emptyList()

            try {
                val productsResponse = productRepository.getProductsFromApi()

                if (productsResponse.isSuccessful && productsResponse.body()?.success == true) {
                    val remoteProducts = productsResponse.body()?.data ?: emptyList()

                    // Tomamos productos para destacados
                    featured = remoteProducts.take(6)

                    // Mapa de categorías
                    val categoryImages = mapOf(
                        "Juegos de Mesa" to R.drawable.catan,
                        "Accesorios" to R.drawable.accesorios,
                        "Consolas" to R.drawable.consolas,
                        "Computadores Gamers" to R.drawable.pcgamer_asus,
                        "Sillas Gamers" to R.drawable.silla_gamer,
                        "Mouse" to R.drawable.mouse_logitech,
                        "Mousepad" to R.drawable.mousepad_razer,
                        "Poleras Personalizadas" to R.drawable.poleragamer_personalizada,
                        "General" to R.drawable.consolas
                    )

                    categoriesList = remoteProducts
                        .map { it.category }
                        .distinct()
                        .map { categoryName ->
                            ProductCategory(
                                name = categoryName,
                                imageRes = categoryImages[categoryName] ?: R.drawable.product
                            )
                        }
                        .take(5)

                } else {
                    Log.e("HomeVM", "Error Backend: ${productsResponse.code()} - ${productsResponse.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeVM", "Error Red Backend", e)
            }

            // 3. ACTUALIZAR ESTADO
            _uiState.value = HomeState(
                featuredProducts = featured,
                categories = categoriesList,
                isLoading = false,
                pokemonName = pokemonNameResult
            )
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            // 1. NUBE
            val token = SessionViewModel.userToken
            if (!token.isNullOrEmpty()) {
                try {
                    val response = productRepository.addToCart(token, product.id, 1)
                    if (response.isSuccessful) {
                        Log.d("HomeVM", "Producto ${product.name} agregado al carrito NUBE")
                    }
                } catch (e: Exception) {
                    Log.e("HomeVM", "Excepción carrito nube", e)
                }
            }

            // 2. LOCAL (DAO)
            try {
                // Nota: Asegúrate de que tu modelo Product tenga 'code' o usa 'id.toString()'
                val codeForLocal = product.id.toString()

                val existingItem = cartDao.getItemByCode(codeForLocal)
                if (existingItem != null) {
                    existingItem.quantity++
                    cartDao.upsertItem(existingItem)
                } else {
                    val cartItem = CartItem(
                        productCode = codeForLocal,
                        productName = product.name,
                        productPrice = product.price,
                        quantity = 1
                    )
                    cartDao.upsertItem(cartItem)
                }
            } catch (e: Exception) {
                Log.e("HomeVM", "Error en carrito local", e)
            }
        }
    }
}
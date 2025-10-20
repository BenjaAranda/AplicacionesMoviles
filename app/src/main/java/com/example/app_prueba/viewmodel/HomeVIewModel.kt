package com.example.app_prueba.viewmodel

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.R
import com.example.app_prueba.application.LevelUpGamerApp
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProductCategory(
    val name: String,
    @DrawableRes val imageRes: Int
)

data class HomeState(
    val featuredProducts: List<Product> = emptyList(),
    val categories: List<ProductCategory> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val productDao = (application as LevelUpGamerApp).database.productDao()
    private val cartDao = (application as LevelUpGamerApp).database.cartDao()

    init {
        loadHomePageContent()
    }

    private fun loadHomePageContent() {
        viewModelScope.launch {
            val allProducts = productDao.getAllProducts().first()
            val featured = allProducts.take(6)

            val categoryImages = mapOf(
                "Juegos de Mesa" to R.drawable.catan,
                "Accesorios" to R.drawable.accesorios,
                "Consolas" to R.drawable.consolas,
                "Computadores Gamers" to R.drawable.pcgamer_asus,
                "Sillas Gamers" to R.drawable.silla_gamer,
                "Mouse" to R.drawable.mouse_logitech,
                "Mousepad" to R.drawable.mousepad_razer,
                "Poleras Personalizadas" to R.drawable.poleragamer_personalizada
            )

            val categories = allProducts
                .map { it.category }
                .distinct()
                .map { categoryName ->
                    ProductCategory(
                        name = categoryName,
                        imageRes = categoryImages[categoryName] ?: R.drawable.product
                    )
                }
                .take(5)

            _uiState.value = HomeState(
                featuredProducts = featured,
                categories = categories,
                isLoading = false
            )
        }
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
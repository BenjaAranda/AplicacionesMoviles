package com.example.app_prueba.repository

import com.example.app_prueba.data.model.AddToCartRequest
import com.example.app_prueba.data.remote.RetrofitInstance

class ProductRepository {
    private val api = RetrofitInstance.api

    // Obtener productos del Backend
    suspend fun getProductsFromApi() = api.listProducts()

    // Obtener carrito del Backend (Necesita el token "Bearer ...")
    suspend fun getCart(token: String) = api.getCart("Bearer $token")

    // Agregar al carrito en Backend
    suspend fun addToCart(token: String, productId: Int, quantity: Int) =
        api.addToCart("Bearer $token", AddToCartRequest(productId, quantity))

    // Crear orden (Checkout)
    suspend fun checkout(token: String) = api.createOrder("Bearer $token")
}
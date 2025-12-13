package com.example.app_prueba.repository

import android.util.Log
import com.example.app_prueba.data.model.AddToCartRequest
import com.example.app_prueba.data.remote.RetrofitInstance

class ProductRepository {
    // Variable privada pero accesible internamente
    val api = RetrofitInstance.api

    // Obtener productos (Lista)
    suspend fun getProductsFromApi() = api.listProducts()

    // Obtener UN producto por ID
    suspend fun getProductById(id: Int) = api.getProduct(id)

    // Obtener carrito (Con LOG de depuración)
    suspend fun getCart(token: String): retrofit2.Response<com.example.app_prueba.data.model.CartListResponse> {
        val authHeader = "Bearer $token"
        Log.d("RepoDebug", "Enviando al carrito HEADER: $authHeader") // <-- MIRA ESTO EN LOGCAT
        return api.getCart(authHeader)
    }

    // Agregar al carrito
    suspend fun addToCart(token: String, productId: Int, quantity: Int): retrofit2.Response<com.example.app_prueba.data.model.SimpleResponse> {
        val authHeader = "Bearer $token"
        Log.d("RepoDebug", "Agregando al carrito HEADER: $authHeader")
        return api.addToCart(authHeader, AddToCartRequest(productId, quantity))
    }

    // Checkout
    suspend fun checkout(token: String) = api.createOrder("Bearer $token")
}
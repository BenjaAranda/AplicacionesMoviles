package com.example.app_prueba.repository

import android.util.Log
import com.example.app_prueba.data.model.AddToCartRequest
import com.example.app_prueba.data.model.CartListResponse // <--- Necesario para getCart
import com.example.app_prueba.data.model.SimpleResponse   // <--- Necesario para respuestas simples
import com.example.app_prueba.data.model.UpdateCartRequest // <--- Necesario para actualizar cantidad
import com.example.app_prueba.data.remote.RetrofitInstance
import retrofit2.Response

class ProductRepository {
    // Variable privada pero accesible internamente
    val api = RetrofitInstance.api

    // Obtener productos (Lista)
    suspend fun getProductsFromApi() = api.listProducts()

    // Obtener UN producto por ID
    suspend fun getProductById(id: Int) = api.getProduct(id)

    // Obtener carrito
    suspend fun getCart(token: String): Response<CartListResponse> {
        val authHeader = "Bearer $token"
        Log.d("RepoDebug", "Enviando al carrito HEADER: $authHeader")
        return api.getCart(authHeader)
    }

    // Agregar al carrito
    suspend fun addToCart(token: String, productId: Int, quantity: Int): Response<SimpleResponse> {
        val authHeader = "Bearer $token"
        Log.d("RepoDebug", "Agregando al carrito HEADER: $authHeader")
        return api.addToCart(authHeader, AddToCartRequest(productId, quantity))
    }

    // Checkout
    suspend fun checkout(token: String) = api.createOrder("Bearer $token")

    // --- FUNCIONES NUEVAS PARA EL CARRITO ---

    // Actualizar cantidad (+ o -)
    suspend fun updateCartItemQuantity(token: String, itemId: Int, newQuantity: Int): Response<SimpleResponse> {
        return api.updateCartQuantity("Bearer $token", itemId, UpdateCartRequest(newQuantity))
    }

    // Eliminar Item específico
    suspend fun deleteCartItem(token: String, itemId: Int): Response<SimpleResponse> {
        return api.deleteCartItem("Bearer $token", itemId)
    }

    // Vaciar Carrito Completo
    suspend fun clearCartCloud(token: String): Response<SimpleResponse> {
        return api.clearCart("Bearer $token")
    }
}
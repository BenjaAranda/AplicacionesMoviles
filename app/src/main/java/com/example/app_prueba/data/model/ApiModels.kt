package com.example.app_prueba.data.model

import com.google.gson.annotations.SerializedName

// Respuesta genérica para mensajes simples
data class SimpleResponse(
    val success: Boolean,
    val message: String
)

// Para agregar al carrito
data class AddToCartRequest(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int
)

// Respuesta del carrito
data class CartListResponse(
    val success: Boolean,
    val data: List<CartItemRemote>
)

data class CartItemRemote(
    val id: Int,
    val quantity: Int,
    @SerializedName("product_id") val productId: Int,
    val name: String,
    val price: Double,
    @SerializedName("image_url") val imageUrl: String?
)

// Respuesta de Órdenes
data class OrderResponse(
    val success: Boolean,
    val message: String,
    val data: OrderData?
)

data class OrderData(
    @SerializedName("order_id") val orderId: Int,
    val total: Double
)
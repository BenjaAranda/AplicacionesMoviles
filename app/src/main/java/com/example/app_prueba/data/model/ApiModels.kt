package com.example.app_prueba.data.model

import com.google.gson.annotations.SerializedName

// --- GENERAL ---
data class SimpleResponse(
    val success: Boolean,
    val message: String
)

// --- AUTH (LOGIN / REGISTER) ---
data class LoginRequest(
    val email: String,
    val pass: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val token: String,
    val user: UserRemote?
)

data class UserRemote(
    val id: Int,
    val email: String,
    val name: String?,
    @SerializedName("is_admin") val isAdmin: Boolean,
    @SerializedName("has_duoc_discount") val hasDuocDiscount: Boolean
)

data class RegisterRequest(
    val email: String,
    val pass: String,
    val name: String,
    @SerializedName("has_duoc_discount") val hasDuocDiscount: Boolean
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

// --- PRODUCTOS ---
// Nota: Asumimos que la clase 'Product' existe en data/model/Product.kt
// Si te da error en 'Product', asegúrate de que Product.kt exista.

data class ProductListResponse(
    val success: Boolean,
    val data: List<Product>
)

data class ProductResponse(
    val success: Boolean,
    val data: Product
)

// --- CARRITO ---

// Para agregar (POST)
data class AddToCartRequest(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int
)

// Para actualizar cantidad (PUT)
data class UpdateCartRequest(
    val quantity: Int
)

// Respuesta del listado (GET)
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

// --- ORDENES ---
data class OrderResponse(
    val success: Boolean,
    val message: String,
    val data: OrderData?
)

data class OrderData(
    @SerializedName("order_id") val orderId: Int,
    val total: Double
)
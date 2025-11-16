// En data/model/ProductResponse.kt
package com.example.app_prueba.data.model

import com.google.gson.annotations.SerializedName

// Clase para la respuesta de la lista de productos
data class ProductListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<Product>? // La lista de productos
)

// Clase para la respuesta de un solo producto
data class ProductDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: Product? // Un solo producto
)
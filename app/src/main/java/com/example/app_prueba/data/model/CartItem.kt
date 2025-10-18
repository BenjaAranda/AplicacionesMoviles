package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productCode: String,
    val productName: String,
    val productPrice: Double,
    var quantity: Int
)
// data/model/Product.kt
package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val code: String,
    val category: String,
    val name: String,
    val price: Double,
    val description: String
)
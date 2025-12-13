package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0, // ID interno de Room (SQLite)

    val productCode: String,
    val productName: String,
    val productPrice: Double,
    var quantity: Int,

    // --- NUEVOS CAMPOS ---
    val id: Int = 0,       // ID del Backend (AWS)
    val imageRes: Int = 0  // Recurso de imagen local
)
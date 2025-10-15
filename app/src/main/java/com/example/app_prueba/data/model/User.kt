// ruta: data/model/User.kt
package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val pass: String, // Nota: En un proyecto real, la contraseña SIEMPRE debe estar encriptada.
    val hasDuocDiscount: Boolean = false // Para el descuento del 20% [cite: 26]
)
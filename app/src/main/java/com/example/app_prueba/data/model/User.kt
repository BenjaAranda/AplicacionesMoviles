package com.example.app_prueba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val email: String,

    @SerializedName("pass")
    val pass: String,

    @SerializedName("has_duoc_discount")
    val hasDuocDiscount: Boolean = false
)

// --- CLASE AÑADIDA ---
// Esta es la clase que se usa para ENVIAR la petición de registro.
// No incluye el 'id' porque la base de datos lo genera.
data class UserRegisterRequest(
    val email: String,
    @SerializedName("pass")
    val pass: String,
    @SerializedName("has_duoc_discount")
    val hasDuocDiscount: Boolean
)

// --- CLASE AÑADIDA ---
// Esta es la clase que esperamos RECIBIR como respuesta del servidor.
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User?
)
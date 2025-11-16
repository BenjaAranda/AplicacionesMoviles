package com.example.app_prueba.data.model

import com.google.gson.annotations.SerializedName

/**
 * Clase para el objeto "data" anidado que devuelve la API
 * al hacer login.
 */
data class AuthData(
    @SerializedName("token")
    val token: String?, // El token JWT

    @SerializedName("user")
    val user: User? // El objeto de usuario
)

/**
 * Clase principal para la respuesta de /api/login y /api/register
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data") // <-- CORREGIDO: Antes apuntaba a 'user'
    val data: AuthData? // Ahora apunta a la clase AuthData
)
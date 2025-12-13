package com.example.app_prueba.data.model

// Modelo para el historial de movimientos
data class HistoryItem(
    val id: String,
    val date: String,
    val event: String,
    val points: String, // Ej: "+150" o "-50"
    val isGain: Boolean, // True si sumó puntos, False si restó
    val expires: String
)

// Modelo para los beneficios del carrusel
data class BenefitItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null // Opcional si quieres cargar imagen de fondo
)

// Modelo para los items de la tienda
data class RewardItem(
    val id: String,
    val name: String,
    val cost: String // Ej: "500 pts"
)

// Modelo general de respuesta del usuario (Resumen)
data class UserPointsSummary(
    val currentLevel: String,
    val progress: Float, // 0.0 a 1.0
    val pointsThisYear: String,
    val pointsAvailable: String,
    val pointsToCaducate: String
)
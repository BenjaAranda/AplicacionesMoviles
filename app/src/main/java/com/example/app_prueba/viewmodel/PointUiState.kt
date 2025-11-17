package com.example.app_prueba.viewmodel


data class PointUiState(
    // --- Resumen ---
    val pointsAvailable: Int = 0,
    val pointsThisYear: Int = 0,
    val pointsToCaducate: String = "0 (en 0 días)",
    val currentLevel: String = "Bronce",
    val progress: Float = 0.0f, // Un valor de 0.0f a 1.0f

    // --- Reglas ---
    val rules: List<String> = emptyList(),

    // --- Historial ---
    val historyFilters: List<String> = listOf("Todos", "Ganados", "Usados", "Anulados"),
    val selectedHistoryFilter: String = "Todos",
    val historyItems: List<HistoryItem> = emptyList(), // La lista filtrada

    // --- Beneficios ---
    val benefits: List<BenefitItem> = emptyList(),

    // --- Recompensas ---
    val rewards: List<RewardItem> = emptyList()
)

// Clases de datos individuales que usa el UiState
data class HistoryItem(
    val id: String,
    val date: String,
    val event: String,
    val points: String,
    val isGain: Boolean,
    val expires: String,
    val type: String //
)

data class BenefitItem(
    val id: String,
    val title: String,
    val description: String
)

data class RewardItem(
    val id: String,
    val name: String,
    val cost: String, // Ej: "200 pts"
    val costNumeric: Int // Ej: 200
)
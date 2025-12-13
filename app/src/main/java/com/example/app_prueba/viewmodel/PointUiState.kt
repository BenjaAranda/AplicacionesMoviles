package com.example.app_prueba.viewmodel

import com.example.app_prueba.data.model.BenefitItem
import com.example.app_prueba.data.model.HistoryItem
import com.example.app_prueba.data.model.RewardItem

data class PointUiState(
    // Estado de carga y error
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Datos del Resumen
    val currentLevel: String = "Bronce",
    val progress: Float = 0.0f,
    val pointsThisYear: String = "0",
    val pointsAvailable: String = "0",
    val pointsToCaducate: String = "0",

    // Datos de Reglas (pueden ser fijas o venir de API)
    val rules: List<String> = listOf(
        "Gana 1 punto por cada $1000 de compra.",
        "Los puntos vencen a los 12 meses.",
        "Sube de nivel para ganar multiplicadores."
    ),

    // Historial
    val historyFilters: List<String> = listOf("Todo", "Ganados", "Canjeados", "Vencidos"),
    val selectedHistoryFilter: String = "Todo",
    val historyItems: List<HistoryItem> = emptyList(),

    // Beneficios y Recompensas
    val benefits: List<BenefitItem> = emptyList(),
    val rewards: List<RewardItem> = emptyList()
)
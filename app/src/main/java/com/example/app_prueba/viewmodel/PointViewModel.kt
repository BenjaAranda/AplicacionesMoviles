package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- 1. CLASES DE DATOS (El Estado de la UI) ---

data class PointUiState(
    // Resumen
    val currentLevel: String = "oro",
    val progress: Float = 0.8f,
    val pointsThisYear: Int = 980,
    val pointsAvailable: Int = 450,
    val pointsToCaducate: String = "30 (en 20 días)",

    // Reglas (Estático, pero puede venir del VM)
    val rules: List<String> = listOf(
        "1 punto por cada \$1.000 gastado",
        "Los puntos se acreditan al despachar el producto"
    ),

    // Historial
    val historyFilters: List<String> = listOf("Todos", "Ganados", "Usados", "Anulados", "Expirados"),
    val selectedHistoryFilter: String = "Todos",
    val historyItems: List<HistoryItem> = emptyList(), // Se carga al iniciar

    // Beneficios
    val benefits: List<BenefitItem> = listOf(
        BenefitItem("💸 Sistema de Descuentos", "Aprovecha precios exclusivos al canjear tus puntos."),
        BenefitItem("🎂 Cupón de Cumpleaños", "Recibe un cupón especial con descuentos por tu cumpleaños."),
        BenefitItem("🚚 Envío Gratis", "Obtén envío gratis en compras seleccionadas usando tus puntos.")
    ),

    // Tienda
    val rewards: List<RewardItem> = listOf(
        RewardItem("1", "Cupón \$5.000", "200 pts"),
        RewardItem("2", "Envío gratis", "150 pts"),
        RewardItem("3", "Terremot", "150 pts"),
        RewardItem("4", "Key Steam Random", "100 pts")
    )
)

// Clases auxiliares para el estado
data class HistoryItem(val id: String, val date: String, val event: String, val points: String, val expires: String, val isGain: Boolean)
data class BenefitItem(val title: String, val description: String)
data class RewardItem(val id: String, val name: String, val cost: String)


// --- 2. EL VIEWMODEL ---

class PointViewModel : ViewModel() {

    // Flujo de estado privado (Mutable) y público (StateFlow)
    private val _uiState = MutableStateFlow(PointUiState())
    val uiState: StateFlow<PointUiState> = _uiState.asStateFlow()

    // Datos "completos" que simularían venir de una base de datos o API
    private val allHistoryItems = listOf(
        HistoryItem("h1", "20/10/2025", "Compra #12345", "+150", "20/10/2026", true),
        HistoryItem("h2", "18/10/2025", "Canje Cupón", "-100", "-", false),
        HistoryItem("h3", "15/10/2025", "Compra #12300", "+50", "15/10/2026", true),
        HistoryItem("h4", "14/10/2025", "Anulación #12290", "-20", "-", false),
        HistoryItem("h5", "10/10/2025", "Puntos Expirados", "-10", "-", false)
    )

    init {
        // Carga el historial inicial (filtro "Todos")
        loadHistory(_uiState.value.selectedHistoryFilter)
    }

    // --- 3. MANEJADORES DE EVENTOS (Lógica) ---

    /**
     * El usuario ha seleccionado un nuevo filtro en el historial.
     */
    fun onFilterSelected(filter: String) {
        // 1. Actualiza el estado con el filtro seleccionado
        _uiState.update { it.copy(selectedHistoryFilter = filter) }
        // 2. Carga los nuevos datos del historial
        loadHistory(filter)
    }

    /**
     * El usuario ha presionado el botón "Canjear" en una recompensa.
     */
    fun onRedeemReward(rewardId: String) {
        // TODO: Implementar la lógica de canje (llamar a API, mostrar diálogo, etc.)
        println("Iniciando canje para la recompensa: $rewardId")
        // Ejemplo: podrías mostrar un diálogo y luego actualizar los puntos
        // _uiState.update { it.copy(pointsAvailable = it.pointsAvailable - 150) }
    }


    // --- 4. LÓGICA PRIVADA ---

    /**
     * Simula la carga de datos (filtrado) según el filtro seleccionado.
     * En una app real, esto sería una consulta a la API o DB.
     */
    private fun loadHistory(filter: String) {
        viewModelScope.launch {
            // Simula una pequeña demora de red/DB
            // delay(300)

            val filteredList = when (filter) {
                "Ganados" -> allHistoryItems.filter { it.isGain }
                "Usados" -> allHistoryItems.filter { !it.isGain && it.points.startsWith("-") && it.event != "Anulación" && it.event != "Puntos Expirados" }
                "Anulados" -> allHistoryItems.filter { it.event.contains("Anulación", ignoreCase = true) }
                "Expirados" -> allHistoryItems.filter { it.event.contains("Expirados", ignoreCase = true) }
                "Todos" -> allHistoryItems
                else -> allHistoryItems
            }

            _uiState.update { it.copy(historyItems = filteredList) }
        }
    }
}

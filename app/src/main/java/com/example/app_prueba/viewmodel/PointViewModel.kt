package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PointViewModel : ViewModel() {

    // --- 1. Definición de Estado (Privado y Público) ---
    private val _uiState = MutableStateFlow(PointUiState())
    val uiState: StateFlow<PointUiState> = _uiState.asStateFlow()

    // --- 2. Datos (Reemplaza los datos 'hardcodeados' de tu JS) ---

    // Datos de ejemplo que vendrían de una base de datos o API
    private val allHistoryItems = listOf(
        HistoryItem("1", "20-09-2025", "Compra - PC", "+70", true, "20-09-2026", "Ganados"),
        HistoryItem("2", "15-09-2025", "Canje - Cupón", "-100", false, "-", "Usados"),
        HistoryItem("3", "10-09-2025", "Compra - Monitor", "+200", true, "10-09-2026", "Ganados"),
        HistoryItem("4", "05-09-2025", "Devolución", "-50", false, "-", "Anulados"),
        HistoryItem("5", "01-09-2025", "Compra - Teclado", "+80", true, "01-09-2026", "Ganados")
    )

    private val allRewards = listOf(
        RewardItem("c1", "Cupón $5.000", "200 pts", 200),
        RewardItem("c2", "Envío gratis", "150 pts", 150),
        RewardItem("c3", "Terremoto", "150 pts", 150) // ;)
    )

    private val allBenefits = listOf(
        BenefitItem("b1", "💸 Sistema de Descuentos", "Aprovecha precios exclusivos al canjear tus puntos."),
        BenefitItem("b2", "🎂 Cupón de Cumpleaños", "Recibe un cupón especial con descuentos por tu cumpleaños."),
        BenefitItem("b3", "🚚 Envío Gratis", "Obtén envío gratis en compras seleccionadas usando tus puntos.")
    )

    // --- 3. Lógica de Puntos (Traducción de tu JS a Kotlin) ---

    // Equivalente a tu objeto 'puntosPorNivel'
    private val puntosPorNivel = mapOf(
        "Bronce" to 0,
        "Plata" to 50,
        "Oro" to 100,
        "Platino" to 200,
        "Diamante" to 350,
        "Esmeralda" to 500
    )

    // Función que se llama cuando el ViewModel se inicia
    init {
        loadData()
    }

    // Carga todos los datos iniciales
    private fun loadData() {
        viewModelScope.launch {
            // Simula una carga de datos (ej: de un usuario)
            val totalPuntosDisponibles = 450
            val totalPuntosAnio = 980

            // Lógica de JS traducida
            val nivel = calcularNivel(totalPuntosDisponibles)
            val progreso = calcularProgreso(totalPuntosDisponibles, nivel)

            _uiState.update { currentState ->
                currentState.copy(
                    // Resumen
                    pointsAvailable = totalPuntosDisponibles,
                    pointsThisYear = totalPuntosAnio,
                    pointsToCaducate = "30 (en 20 días)", // (Debería venir de la API)
                    currentLevel = nivel,
                    progress = progreso,

                    // Reglas (de tu HTML)
                    rules = listOf(
                        "1 punto por cada $1.000 gastado",
                        "Los puntos se acreditan al despachar el producto"
                    ),

                    // Listas
                    historyItems = getFilteredHistory("Todos"), // Muestra "Todos" al inicio
                    benefits = allBenefits,
                    rewards = allRewards
                )
            }
        }
    }

    // --- 4. Funciones que la UI puede llamar (Eventos) ---


    fun onFilterSelected(filter: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedHistoryFilter = filter,
                historyItems = getFilteredHistory(filter)
            )
        }
    }


    fun onRedeemReward(rewardId: String) {
        val reward = allRewards.find { it.id == rewardId } ?: return
        val currentPoints = _uiState.value.pointsAvailable

        if (currentPoints >= reward.costNumeric) {
            // 1. Restar puntos (habría que llamar a la API)
            val newPoints = currentPoints - reward.costNumeric

            // 2. Recalcular nivel y progreso
            val newLevel = calcularNivel(newPoints)
            val newProgress = calcularProgreso(newPoints, newLevel)

            // 3. Actualizar la UI
            _uiState.update { it.copy(
                pointsAvailable = newPoints,
                currentLevel = newLevel,
                progress = newProgress
            ) }


            println("¡Canjeado ${reward.name}!")
        } else {
            println("Puntos insuficientes para ${reward.name}")
        }
    }

    // --- 5. Funciones Privadas (Lógica interna) ---


    private fun calcularNivel(puntos: Int): String {
        // Filtra los niveles que el usuario ha alcanzado y toma el más alto
        return puntosPorNivel
            .filter { it.value <= puntos }
            .maxByOrNull { it.value }
            ?.key ?: "Bronce" // Si no encuentra, es Bronce
    }

    /**
     * Equivalente a tu `calcularProgreso(puntos)` de JS
     */
    private fun calcularProgreso(puntos: Int, nivelActual: String): Float {
        val niveles = puntosPorNivel.keys.toList()
        val valores = puntosPorNivel.values.toList()

        val indiceNivel = niveles.indexOf(nivelActual)

        // Si es el nivel máximo, la barra está al 100%
        if (indiceNivel == niveles.size - 1) return 1.0f

        val puntosNivelActual = valores[indiceNivel]
        val puntosNivelSiguiente = valores[indiceNivel + 1]

        val rango = (puntosNivelSiguiente - puntosNivelActual).toFloat()
        val avance = (puntos - puntosNivelActual).toFloat()

        // Evita división por cero si el rango es 0
        if (rango == 0f) return 0f

        return (avance / rango).coerceIn(0.0f, 1.0f) // Asegura que esté entre 0 y 1
    }


    private fun getFilteredHistory(filter: String): List<HistoryItem> {
        if (filter == "Todos") {
            return allHistoryItems
        }
        return allHistoryItems.filter { it.type == filter }
    }
}
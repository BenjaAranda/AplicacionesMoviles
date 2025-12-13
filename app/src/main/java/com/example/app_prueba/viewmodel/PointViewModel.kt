package com.example.app_prueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_prueba.data.repository.PointsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PointViewModel : ViewModel() {

    // Inicializa el repositorio
    private val repository = PointsRepository()

    // Estado interno mutable
    private val _uiState = MutableStateFlow(PointUiState())
    // Estado público inmutable para la UI
    val uiState: StateFlow<PointUiState> = _uiState.asStateFlow()

    init {
        // Cargar datos iniciales al abrir la pantalla
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Hacemos las llamadas en paralelo o secuencia
                val summary = repository.getSummary()
                val benefits = repository.getBenefits()
                val rewards = repository.getRewards()

                // Cargamos el historial con el filtro por defecto
                val history = repository.getHistory(_uiState.value.selectedHistoryFilter)

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        currentLevel = summary?.currentLevel ?: "N/A",
                        progress = summary?.progress ?: 0f,
                        pointsThisYear = summary?.pointsThisYear ?: "0",
                        pointsAvailable = summary?.pointsAvailable ?: "0",
                        pointsToCaducate = summary?.pointsToCaducate ?: "0",
                        benefits = benefits,
                        rewards = rewards,
                        historyItems = history
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    // Evento: Usuario selecciona un filtro de historial
    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(selectedHistoryFilter = filter, isLoading = true) }

        viewModelScope.launch {
            val filteredHistory = repository.getHistory(filter)
            _uiState.update {
                it.copy(
                    historyItems = filteredHistory,
                    isLoading = false
                )
            }
        }
    }

    // Evento: Usuario canjea una recompensa
    fun onRedeemReward(rewardId: String) {
        viewModelScope.launch {
            val success = repository.redeemReward(rewardId)
            if (success) {
                // Si tuvo éxito, recargamos el resumen (porque bajaron los puntos)
                val newSummary = repository.getSummary()
                _uiState.update {
                    it.copy(
                        pointsAvailable = newSummary?.pointsAvailable ?: it.pointsAvailable
                        // Aquí podrías mostrar un Toast o Snackbar de éxito
                    )
                }
            }
        }
    }
}
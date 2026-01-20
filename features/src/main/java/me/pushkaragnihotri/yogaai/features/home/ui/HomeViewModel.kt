package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.model.DailyMetric
import me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.core.data.repository.WellnessRepository

data class HomeUiState(
    val riskPrediction: RiskPrediction? = null,
    val metrics: DailyMetric = DailyMetric(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val repository: WellnessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.todayMetrics.collect { metrics ->
                _uiState.update { it.copy(metrics = metrics) }
            }
        }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val risk = repository.getTodayRisk()
                repository.refreshMetrics()
                _uiState.update {
                    it.copy(
                        riskPrediction = risk,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

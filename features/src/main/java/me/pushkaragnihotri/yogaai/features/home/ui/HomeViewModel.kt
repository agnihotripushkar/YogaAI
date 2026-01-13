package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Parallel fetch could be better but sequential is fine for now
                val risk = repository.getTodayRisk()
                val metrics = repository.getTodayMetrics()
                _uiState.value = HomeUiState(
                    riskPrediction = risk,
                    metrics = metrics,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Handle error
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

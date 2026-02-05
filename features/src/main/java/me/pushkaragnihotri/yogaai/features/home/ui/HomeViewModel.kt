package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.model.RiskPrediction
import me.pushkaragnihotri.yogaai.core.repository.WellnessRepository
import me.pushkaragnihotri.yogaai.features.home.domain.GetDailyWellnessUseCase
import me.pushkaragnihotri.yogaai.features.home.model.WellnessUiModel

data class HomeUiState(
    val riskPrediction: RiskPrediction? = null,
    val wellnessItems: List<WellnessUiModel> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val repository: WellnessRepository,
    private val getDailyWellnessUseCase: GetDailyWellnessUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDailyWellnessUseCase().collect { items ->
                _uiState.update { it.copy(wellnessItems = items) }
            }
        }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Risk is still fetched directly from repo for now (could be another use case)
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

package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.home.domain.HomeRepository
import me.pushkaragnihotri.yogaai.features.home.data.model.WellnessUiModel

data class HomeUiState(
    val riskPrediction: RiskPrediction? = null,
    val wellnessItems: List<WellnessUiModel> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            homeRepository.refreshMetrics()
            val metrics = homeRepository.getTodayMetrics()
            val prediction = homeRepository.getTodayRisk()
            
            // Initial UI update with fetched data
             _uiState.update {
                it.copy(
                     riskPrediction = prediction,
                     isLoading = false
                )
             }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Refresh and fetch again to be sure
                homeRepository.refreshMetrics()
                val risk = homeRepository.getTodayRisk()
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

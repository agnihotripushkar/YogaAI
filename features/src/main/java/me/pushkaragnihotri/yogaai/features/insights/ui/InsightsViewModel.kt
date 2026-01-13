package me.pushkaragnihotri.yogaai.features.insights.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.core.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.core.data.repository.WellnessRepository

class InsightsViewModel(
    private val repository: WellnessRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<RiskPrediction>>(emptyList())
    val history: StateFlow<List<RiskPrediction>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getRiskHistory()
        }
    }
}

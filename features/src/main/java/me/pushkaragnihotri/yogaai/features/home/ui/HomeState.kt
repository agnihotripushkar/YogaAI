package me.pushkaragnihotri.yogaai.features.home.ui

import me.pushkaragnihotri.yogaai.core.presentation.UiText
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction

data class HomeState(
    val riskPrediction: RiskPrediction? = null,
    val wellnessItems: List<WellnessUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val hasPermissions: Boolean = false,
    val sdkAvailable: Boolean = false,
    val error: UiText? = null
)

package me.pushkaragnihotri.yogaai.features.yoga.ui

import me.pushkaragnihotri.yogaai.core.presentation.UiText

sealed interface YogaDetectorEvent {
    data class NavigateToResult(
        val poseName: String,
        val duration: String,
        val feedback: String
    ) : YogaDetectorEvent
    data class ShowError(val message: UiText) : YogaDetectorEvent
}

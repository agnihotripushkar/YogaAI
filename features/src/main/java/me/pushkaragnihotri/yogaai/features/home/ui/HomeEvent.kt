package me.pushkaragnihotri.yogaai.features.home.ui

import me.pushkaragnihotri.yogaai.core.presentation.UiText

sealed interface HomeEvent {
    data class RequestPermissions(val permissions: Set<String>) : HomeEvent
    data class ShowSnackbar(val message: UiText) : HomeEvent
}

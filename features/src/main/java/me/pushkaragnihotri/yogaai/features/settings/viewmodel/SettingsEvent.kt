package me.pushkaragnihotri.yogaai.features.settings.ui

sealed interface SettingsEvent {
    data class RequestPermissions(val permissions: Set<String>) : SettingsEvent
    data class OpenUrl(val url: String) : SettingsEvent
    data object RateApp : SettingsEvent
    data class ShareApp(val text: String) : SettingsEvent
    data object NavigateToAppearance : SettingsEvent
}

package me.pushkaragnihotri.yogaai.features.settings.ui

sealed interface SettingsAction {
    data class OnThemeChange(val mode: Int) : SettingsAction
    data class OnLanguageChange(val language: String) : SettingsAction
    data object OnConnectClick : SettingsAction
    data class OnPermissionsResult(val granted: Set<String>) : SettingsAction
    data object OnDisconnectWearable : SettingsAction
    data object OnDeleteData : SettingsAction
    data object OnPrivacyPolicyClick : SettingsAction
    data object OnInitialLoad : SettingsAction
}

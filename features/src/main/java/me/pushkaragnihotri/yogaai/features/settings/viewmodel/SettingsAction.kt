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
    data class OnDynamicColorChange(val enabled: Boolean) : SettingsAction
    data object OnRateAppClick : SettingsAction
    data object OnShareAppClick : SettingsAction
    data object OnProfileEditorOpen : SettingsAction
    data object OnProfileEditorDismiss : SettingsAction
    data class OnProfileSave(val name: String, val age: Int, val level: String) : SettingsAction
}

package me.pushkaragnihotri.yogaai.features.settings.ui

data class SettingsState(
    val themeMode: Int = 0,
    val language: String = "English",
    val steps: Long = 0L,
    val calories: Double = 0.0,
    val hasPermissions: Boolean = false,
    val sdkAvailable: Boolean = false,
    val dynamicColor: Boolean = false
)

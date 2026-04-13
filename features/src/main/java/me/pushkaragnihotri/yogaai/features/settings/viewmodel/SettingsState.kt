package me.pushkaragnihotri.yogaai.features.settings.ui

data class SettingsState(
    val themeMode: Int = 0,
    val language: String = "English",
    val dynamicColor: Boolean = false,
    val userName: String = "",
    val userAge: Int = 0,
    val userSex: String = "",
    val userHeight: Int = 0,
    val userWeight: Float = 0f,
    val userTargetWeight: Float = 0f,
    val userLevel: String = "",
    val appVersionName: String = "",
    val appVersionCode: Long = 0L,
    val showProfileEditor: Boolean = false
)
